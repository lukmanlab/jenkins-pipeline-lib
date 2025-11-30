package pipelines.utils

class Docker implements Serializable {
    static String buildTag(Map config) {
        def env = Common.buildEnvironment(config)
        def tag = "${config.dockerRegistry}/${config.registryNamespace}/${config.projectName}:${config.imageTag}-${env}"
        return tag
    }
}
