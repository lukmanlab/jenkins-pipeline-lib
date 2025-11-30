package pipelines.stages

import pipelines.core.PipelineStage
import pipelines.utils.Docker

class DockerPushStage implements PipelineStage, Serializable {
    private def script

    DockerPushStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def tag = Docker.buildTag(config)
        script.echo "Pushing Docker image: ${tag}"

        if (config.dockerCredentials) {
            pushWithCredential(tag, config)
        } else if (config.gcrServiceAccount) {
            pushWithServiceAccount(tag, config)
        } else {
            pushWithoutCredential(tag)
        }
    }

    private def pushWithCredential(String tag, Map config) {
        script.withCredentials([script.usernamePassword(
                credentialsId: config.dockerCredentials,
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
        )]) {
            script.sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

            script.sh "docker push ${tag}"

            // Logout for security
            script.sh 'docker logout'
        }
    }

    private def pushWithoutCredential(String tag) {
        script.sh "docker push ${tag}"
    }

    private def pushWithServiceAccount(String tag, Map config) {
        def registry = Docker.getRegistry(config)
        script.sh "gcloud config set auth/impersonate_service_account ${config.gcrServiceAccount}"
        script.sh "gcloud auth configure-docker ${registry}"
        script.sh "docker push ${tag}"
    }
}
