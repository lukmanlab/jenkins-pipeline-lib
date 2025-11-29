package pipelines.services

interface SourceControl extends Serializable {
    def checkout(Map config)
}
