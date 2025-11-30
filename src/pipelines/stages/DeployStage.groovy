package pipelines.stages

import pipelines.core.PipelineStage
import pipelines.utils.Common

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

        if (result.platform == 'gcp_cloud_run' && config.deployServiceAccount) {
            script.sh "gcloud config set auth/impersonate_service_account ${config.deployServiceAccount}"
        }

        script.sh "${result.command} ${config.projectName} ${result.options} ${serviceAccountOpt} ${additionalOptions}"
    }
}
