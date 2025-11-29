package pipelines.stages

import pipelines.core.PipelineStage

class DockerBuildStage implements PipelineStage, Serializable {
    private def script

    DockerBuildStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def tag = "${config.dockerRegistry}/${config.registryNamespace}/${config.projectName}:${config.imageTag}"
        script.echo "Building Docker image: ${tag}"
        script.sh "docker build -t ${tag} ."
    }
}
