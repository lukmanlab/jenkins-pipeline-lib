package pipelines.stages

import pipelines.core.PipelineStage

class DeployStage implements PipelineStage, Serializable {
    private def script

    DeployStage(script) {
        this.script = script
    }

    def execute(Map config) {
        script.echo "Deploying to ${config.environment}..."
        script.sh "${config.deployCommand}"
    }
}
