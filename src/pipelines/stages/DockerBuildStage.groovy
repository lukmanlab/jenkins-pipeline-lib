package pipelines.stages

class DockerBuildStage implements Serializable {
    private def script

    DockerBuildStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def tag = "${config.dockerRegistry}/${config.imageName}:${config.imageTag}"
        script.echo "Building Docker image: ${tag}"
        script.sh "docker build -t ${tag} ."
    }
}
