package pipelines.utils

class Docker implements Serializable {
    static String getRegistry(Map config) {
        def registry = config.dockerRegistry ?: 'asia-southeast2-docker.pkg.dev'
        return registry
    }

    static String buildTag(Map config) {
        def env = Common.buildEnvironment(config)
        def registry = getRegistry(config)
        def tag = "${registry}/${config.registryNamespace}/${config.projectName}:${config.imageTag}-${env}"
        return tag
    }
}
