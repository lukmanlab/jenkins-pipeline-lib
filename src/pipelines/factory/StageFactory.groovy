package pipelines.factory

import pipelines.core.PipelineStage
import pipelines.stages.*

class StageFactory implements Serializable {
    private def script

    StageFactory(script) {
        this.script = script
    }

    PipelineStage createStage(String type) {
        switch(type.toLowerCase()) {
            case 'checkout':
                return new CheckoutStage(script) as PipelineStage
            case 'build':
                return new BuildStage(script) as PipelineStage
            case 'dockerbuild':
                return new DockerBuildStage(script) as PipelineStage
            case 'dockerpush':
                return new DockerPushStage(script) as PipelineStage
            case 'deploy':
                return new DeployStage(script) as PipelineStage
            default:
                throw new IllegalArgumentException("Unknown stage type: ${type}")
        }
    }
}
