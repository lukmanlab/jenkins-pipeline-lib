package pipelines.core

abstract class BasePipelineStage implements PipelineStage, Serializable {
    protected def script
    protected String stageName

    BasePipelineStage(script, String stageName) {
        this.script = script
        this.stageName = stageName
    }

    def execute(Map config) {
        script.stage(stageName) {
            preExecute(config)
            doExecute(config)
            postExecute(config)
        }
    }

    abstract def doExecute(Map config)

    def preExecute(Map config) {
        script.echo "=== Starting: ${stageName} ==="
    }

    def postExecute(Map config) {
        script.echo "=== Completed: ${stageName} ==="
    }
}
