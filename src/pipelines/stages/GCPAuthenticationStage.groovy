package pipelines.stages

import pipelines.core.PipelineStage

class GCPAuthenticationStage implements PipelineStage, Serializable {
    private def script

    GCPAuthenticationStage(script) {
        this.script = script
    }

    def execute(Map config) {
        script.echo "Authentication with Google Cloud"

        script.withCredentials([script.file(
            credentialsId: config.gcpServiceAccount,
            variable: 'GOOGLE_APPLICATION_CREDENTIALS'
        )]) {
            script.sh 'gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS'
            script.sh "gcloud config set project ${config.gcpProjectId}"
            script.sh "gcloud auth list"
        }
    }
}
