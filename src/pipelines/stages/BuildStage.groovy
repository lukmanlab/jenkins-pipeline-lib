package pipelines.stages

class BuildStage implements Serializable {
    private def script

    BuildStage(script) {
        this.script = script
    }

    def execute(Map config) {
        script.echo "Building ${config.projectName}..."
        script.sh "${config.buildCommand}"
    }
}
