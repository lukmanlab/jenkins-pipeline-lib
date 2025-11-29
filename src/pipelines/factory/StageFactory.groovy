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
                return new CheckoutStage(script)
            case 'gcpauth':
                return new GCPAuthenticationStage(script)
            case 'build':
                return new BuildStage(script)
            case 'dockerbuild':
                return new DockerBuildStage(script)
            case 'dockerpush':
                return new DockerPushStage(script)
            case 'deploy':
                return new DeployStage(script)
            default:
                throw new IllegalArgumentException("Unknown stage type: ${type}")
        }
    }
}
