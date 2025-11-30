package pipelines.stages

import pipelines.core.PipelineStage
import pipelines.utils.Common
import pipelines.utils.Docker

class DeployStage implements PipelineStage, Serializable {
    private def script

    DeployStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def result = Common.getCommandPlatformDeployment(config)

        script.echo "Deploying to ${config.environment} ${result.platform}"

        if (config.deployServiceAccount) {
            script.sh "gcloud config set auth/impersonate_service_account ${config.deployServiceAccount}"
        }

        if (result.platform == 'gcp_cloud_run_kustomize') {
            def env = Common.buildEnvironment(config)
            def tag = Docker.buildTag(config)
            def kustomizeDir = env == 'production' ? 'deploy/cloudrun/migration/overlays/production' : 'deploy/cloudrun/migration/overlays/development'

            script.sh "kustomize build ${kustomizeDir} > service.yaml"

            script.sh """
                sed "s#${config.imagePlaceholder}#${tag}#g" service.yaml > service.yaml.rendered
            """

            script.sh "${result.command} ${result.options}"

        } else {
            script.sh "platform not supported"
        }
    }
}
