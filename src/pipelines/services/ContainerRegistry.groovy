package pipelines.services

interface ContainerRegistry extends Serializable {
    def push(Map config)
    def pull(Map config)
}