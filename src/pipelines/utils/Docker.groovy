package pipelines.utils

class Docker implements Serializable {
    static String buildTag(Map config) {
        def tag = "${config.dockerRegistry}/${config.registryNamespace}/${config.projectName}:${config.imageTag}"
        return tag
    }
}
