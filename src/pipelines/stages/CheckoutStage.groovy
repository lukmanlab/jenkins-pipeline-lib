package pipelines.stages

import pipelines.core.PipelineStage

class CheckoutStage implements PipelineStage, Serializable {
    private def script

    CheckoutStage(script) {
        this.script = script
    }

    def execute(Map config) {
        script.echo "Checking out code from ${config.repoUrl}"
        script.checkout([
                $class: 'GitSCM',
                branches: [[ name: config.branch ?: '*/main' ]],
                userRemoteConfigs: [[ url: config.repoUrl ]]
        ])
    }
}