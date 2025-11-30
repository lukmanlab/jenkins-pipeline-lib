package pipelines.stages

import pipelines.core.PipelineStage
import pipelines.utils.Docker

class DockerBuildStage implements PipelineStage, Serializable {
    private def script

    DockerBuildStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def tag = Docker.buildTag(config)
        script.echo "Building Docker image: ${tag}"
        script.sh "docker build -t ${tag} ."
    }
}
