package pipelines.core

import pipelines.notifications.Notifier
import pipelines.utils.Docker

class PipelineOrchestrator implements Serializable {
    private def script
    private Map<String, PipelineStage> stages = [:]
    private List<Notifier> notifiers = []

    PipelineOrchestrator(script) {
        this.script = script
    }

    def addStage(String name, PipelineStage stage) {
        stages[name] = stage
        return this
    }

    def addNotifier(Notifier notifier) {
        notifiers.add(notifier)
        return this
    }

    def executeStage(String name, Map config) {
        // checker if the stage is not found
        if (!stages.containsKey(name)) {
            script.error "Stage '${name}' not found"
        }

        stages[name].execute(config)
    }

    def notifyAll(String message, String status) {
        notifiers.each { notifier ->
            try {
                notifier.notify(message, status)
            } catch (Exception e) {
                script.echo "There is error in notification process: ${e.message}"
            }
        }
    }

    def removeDockerImage(Map config) {
        def tag = Docker.buildTag(config)
        if (config.alwaysRemoveImage == 'true') {
            script.sh "docker rmi ${tag}"
        } else {
            script.echo "Doesn't remove image because the 'alwaysRemoveImage' not set or false"
        }
    }
}
