package pipelines.utils

class Common implements Serializable {
    static String buildEnvironment(Map config) {
        switch (config.environment) {
            case 'master':
                // fallthrough
            case 'staging':
                // fallthrough
            case 'main':
                return 'staging'
                break
            case 'production':
                return 'production'
                break
            case 'migration-prod':
                return 'production'
                break
            default:
                return 'staging'
                break
        }
    }

    static Map getCommandPlatformDeployment(Map config) {
        def region = getRegion(config)

        switch (config.deployTo) {
            case 'gcp_cloud_run_kustomize':
                return [
                    platform: 'gcp_cloud_run_kustomize',
                    command: 'gcloud run services replace',
                    options: "service.yaml.rendered --region=${region} --project=${config.deployGcpProjectId}"
                ]
            default:
                return [
                    platform: '',
                    command : '',
                    options: ''
                ]
        }
    }

    static String getRegion(Map config) {
        def region = config.region ?: 'asia-southeast2'
        return region
    }
}
