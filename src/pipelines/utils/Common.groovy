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
}
