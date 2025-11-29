package pipelines.core

interface PipelineStage extends Serializable {
    def execute(Map config)
}
