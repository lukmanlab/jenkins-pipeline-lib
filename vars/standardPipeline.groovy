import pipelines.core.PipelineOrchestrator
import pipelines.notifications.Notifier
import pipelines.factory.StageFactory

def call(Map config) {
    // Initialize orchestrator
    def orchestrator = new PipelineOrchestrator(this)
    def factory = new StageFactory(this)

    // Register stages
    config.stages.each { stageName ->
        orchestrator.addStage(stageName as String, factory.createStage(stageName as String))
    }

    // Register notifiers
    if (config.emailNotifications) {
        orchestrator.addNotifier(new EmailNotifier(this, config.emailRecipient as String) as Notifier)
    }

    pipeline {
        agent any

        options {
            timestamps()
            timeout(time: config.timeout ?: 60, unit: 'MINUTES')
            buildDiscarder(logRotator(numToKeepStr: '10'))
        }

        environment {
            PROJECT_NAME = config.projectName
            ENVIRONMENT = config.environment ?: 'dev'
            BUILD_VERSION = "${env.BUILD_NUMBER}"
        }

        stages {
            stage('Checkout') {
                when {
                    expression { config.stages.contains('checkout') }
                }
                steps {
                    script {
                        orchestrator.executeStage('checkout', config)
                    }
                }
            }

            stage('Build') {
                when {
                    expression {
                        config.stages.contains('build') || config.stages.contains('gradle')
                    }
                }
                steps {
                    script {
                         if (config.stages.contains('gradle')) {
                            orchestrator.executeStage('gradle', config)
                        } else {
                            orchestrator.executeStage('build', config)
                        }
                    }
                }
            }

            stage('Docker Build') {
                when {
                    expression { config.stages.contains('dockerbuild') }
                }
                steps {
                    script {
                        orchestrator.executeStage('dockerbuild', config)
                    }
                }
            }

            stage('Docker Push') {
                when {
                    expression { config.stages.contains('dockerpush') }
                }
                steps {
                    script {
                        orchestrator.executeStage('dockerpush', config)
                    }
                }
            }

            stage('Deploy') {
                when {
                    expression { config.stages.contains('deploy') }
                }
                steps {
                    script {
                        orchestrator.executeStage('deploy', config)
                    }
                }
            }
        }

        post {
            success {
                script {
                    orchestrator.notifyAll(
                            "Build completed successfully for ${config.projectName}",
                            'SUCCESS'
                    )
                }
            }
            failure {
                script {
                    orchestrator.notifyAll(
                            "Build failed for ${config.projectName}",
                            'FAILURE'
                    )
                }
            }
            always {
                cleanWs()
            }
        }
    }
}
