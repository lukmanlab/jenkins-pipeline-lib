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

        script.echo "Deploying to ${config.environment} ${result.platform}"

        script.sh "${result.command} ${config.projectName} ${result.options} ${additionalOptions}"
    }
}
