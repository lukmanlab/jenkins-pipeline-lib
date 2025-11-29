package pipelines.services.impl

import pipelines.services.SourceControl

class GitSourceControl implements SourceControl, Serializable {
    private def script

    GitSourceControl(script) {
        this.script = script
    }

    def checkout(Map config) {
        script.checkout([
                $class: 'GitSCM',
                branches: [[name: config.branch ?: '*/main']],
                userRemoteConfigs: [[url: config.repoUrl]]
        ])
    }
}
