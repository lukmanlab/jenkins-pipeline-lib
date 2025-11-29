package pipelines.stages

import pipelines.core.BasePipelineStage

class GradleBuildStage extends BasePipelineStage {
    GradleBuildStage(script) {
        super(script, 'Gradle Build')
    }

    def doExecute(Map config) {
        script.sh "./gradlew build ${config.gradleArgs ?: ''}"
    }
}
