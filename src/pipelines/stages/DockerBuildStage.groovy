package pipelines.stages

import pipelines.core.PipelineStage
import pipelines.utils.Common
import pipelines.utils.Docker

class DockerBuildStage implements PipelineStage, Serializable {
    private def script

    DockerBuildStage(script) {
        this.script = script
    }

    def execute(Map config) {
        def tag = Docker.buildTag(config)
        def env = Common.buildEnvironment(config)
        def dockerFile = env == 'production' ? 'Dockerfile.production' : 'Dockerfile.staging'
        def dockerFileOption = config.useEnvDockerfile == 'true' ? "-f ${dockerFile}" : ''
        def dockerBuildArgs = config.dockerBuildArgs ?: ''

        script.echo "Building Docker image: ${tag}"
        script.sh "docker build ${dockerFileOption} ${dockerBuildArgs} -t ${tag} ."
    }
}
