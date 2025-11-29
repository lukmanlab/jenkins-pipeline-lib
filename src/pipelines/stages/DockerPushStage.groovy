package pipelines.stages

class DockerPushStage implements Serializable {
    private def script

    DockerPushStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def tag = "${config.dockerRegistry}/${config.imageName}:${config.imageTag}"
        script.echo "Pushing Docker image: ${tag}"
        script.withCredentials([script.usernamePassword(
                credentialsId: config.dockerCredentials,
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
        )]) {
            script.sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
            script.sh "docker push ${tag}"
        }
    }
}
