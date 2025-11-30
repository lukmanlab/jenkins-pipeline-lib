package pipelines.utils

class Common implements Serializable {
    static String buildEnvironment(Map config) {
        switch (config.environment) {
            case 'master':
                // fallthrough
            case 'main':
                return 'staging'
                break
            case 'production':
                return 'production'
                break
            default:
                return 'staging'
                break
        }
    }

    static Map getCommandPlatformDeployment(Map config) {
        def tag = Docker.buildTag(config)
        def region = config.region ?: 'asia-southeast2'

        switch (config.deployTo) {
            case 'gcp_cloud_run':
                return [
                    platform: 'gcp_cloud_run',
                    command : 'gcloud run deploy',
                    options : "--image=${tag} --platform=managed --region=${region} --allow-unauthenticated"
                ]
            default:
                return [
                    platform: '',
                    command : '',
                    options: ''
                ]
        }
    }
}
