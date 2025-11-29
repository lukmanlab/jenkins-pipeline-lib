package pipelines.stages

class DeployStage implements Serializable {
    private def script

    DeployStage(script) {
        this.script = script
    }

    def execute(Map config) {
        script.echo "Deploying to ${config.environment}..."
        script.sh "${config.deployCommand}"
    }
}
