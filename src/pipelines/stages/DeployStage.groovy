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
        def additionalOptions = config.additionalOptions ?: ''
        def serviceAccountOpt = config.deployServiceAccount ? "--service-account=${config.deployServiceAccount}" : ''

        script.echo "Deploying to ${config.environment} ${result.platform}"

        if (config.deployServiceAccount) {
            script.sh "gcloud config set auth/impersonate_service_account ${config.deployServiceAccount}"
        }

        if (result.platform == 'gcp_cloud_run_kustomize') {
            def env = Common.buildEnvironment(config)
            def tag = Docker.buildTag(config)
            def kustomizeDir = env == 'production' ? 'deploy/cloudrun/overlays/production' : 'deploy/cloudrun/overlays/development'

            script.sh "kustomize build ${kustomizeDir} > service.yaml"
            script.sh """
                sed "s#${config.searchImageToReplace}#${tag}#g; s#${config.searchSaToReplace}#${config.deployServiceAccount}#g" service.yaml > service.yaml.rendered
            """
            script.sh "${result.command} replace ${result.options}"
            script.sh "${result.command} update ${additionalOptions}"

        } else if (result.platform == 'gcp_cloud_run') {
            script.sh "${result.command} ${config.projectName} ${result.options} ${serviceAccountOpt} ${additionalOptions}"

        } else {
            script.sh "platform not supported"
        }
    }
}
