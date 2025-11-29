package pipelines.notifications

class EmailNotifier implements Notifier, Serializable {
    private def script
    private String recipient

    EmailNotifier(script, String recipient) {
        this.script = script
        this.recipient = recipient
    }

    def notify(String message, String status) {
        def subject = "Pipeline ${status}: ${script.env.JOB_NAME}"
        def body = """
            Build ${status}
            
            Job: ${script.env.JOB_NAME}
            Build: ${script.env.BUILD_NUMBER}
            
            Message: ${message}
            
            URL: ${script.env.BUILD_URL}
        """

        script.emailext(
                subject: subject,
                body: body,
                to: recipient
        )
    }
}
