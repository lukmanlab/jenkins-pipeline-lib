// Example 1: Simple Maven Project
- Jenkinsfile
```groovy
@Library('shared-pipeline-library') _

standardPipeline(
    projectName: 'my-java-app',
    environment: 'dev',
    stages: ['checkout', 'maven', 'test'],
    timeout: 30,

    repoUrl: 'https://github.com/myorg/my-java-app.git',
    branch: '*/develop',
    mavenArgs: '-DskipTests=false',
    testCommand: 'mvn test',
    publishTestResults: true,
    
    emailNotifications: true,
    emailRecipient: 'dev-team@example.com'
)
```

// Example 2: Full CI/CD with Docker
- Jenkinsfile
```groovy
@Library('shared-pipeline-library') _

standardPipeline(
    projectName: 'my-microservice',
    environment: 'production',
    stages: ['checkout', 'gradle', 'test', 'sonar', 'dockerbuild', 'dockerpush', 'deploy'],
    timeout: 45,

    repoUrl: 'https://github.com/myorg/microservice.git',
    branch: '*/main',
    
    gradleArgs: '--no-daemon',
    testCommand: './gradlew test',
    publishTestResults: true,
    
    sonarServer: 'SonarQube',
    waitForQualityGate: true,
    
    dockerRegistry: 'registry.example.com',
    imageName: 'my-microservice',
    imageTag: "${env.BUILD_NUMBER}",
    dockerCredentials: 'docker-hub-credentials',
    
    deployCommand: 'kubectl apply -f k8s/deployment.yaml',
    
    emailNotifications: true,
    emailRecipient: 'ops-team@example.com',
    slackNotifications: true,
    slackChannel: '#deployments'
)
```
// Example 3: Multi-environment deployment
- Jenkinsfile
```groovy
@Library('shared-pipeline-library') _

def deployToEnv(env) {
    standardPipeline(
        projectName: 'my-app',
        environment: env,
        stages: ['checkout', 'maven', 'test', 'dockerbuild', 'dockerpush', 'deploy'],

        repoUrl: 'https://github.com/myorg/my-app.git',
        branch: env == 'prod' ? '*/main' : '*/develop',
        
        dockerRegistry: 'registry.example.com',
        imageName: "my-app-${env}",
        imageTag: "${env}-${env.BUILD_NUMBER}",
        dockerCredentials: 'docker-credentials',
        
        deployCommand: "helm upgrade --install my-app ./helm --set environment=${env}",
        
        slackNotifications: true,
        slackChannel: "#${env}-deployments"
    )
}

// Deploy based on branch
if (env.BRANCH_NAME == 'main') {
    deployToEnv('prod')
} else if (env.BRANCH_NAME == 'develop') {
    deployToEnv('dev')
}

```
