package pipelines.notifications

interface Notifier extends Serializable {
    def notify(String message, String status)
}
