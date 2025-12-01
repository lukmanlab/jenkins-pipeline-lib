- Jenkinsfile
```groovy
@Library('shared-pipeline-library') _

standardPipeline(
        projectName: "${env.JOB_BASE_NAME}",
        environment: "${params.BRANCH_NAME}",
        stages: ['checkout', 'gcpauth', 'dockerbuild', 'dockerpush', 'deploy'],
        timeout: 30,

        // CHECKOUT REPO
        githubCredentialId: 'github-pat',
        repoUrl: "https://github.com/exampleorg/${env.JOB_BASE_NAME}.git",
        branch: "${params.BRANCH_NAME}", // default branch is main (if not set)

        // AUTH GCP
        gcpServiceAccount: 'ci-cd-jenkins',
        /** gcpProjectId is a destination gcp projectId to store the artifact or to autenticate for specific projectId
         If this is configured, the push stage using this projectId
         */
        gcpProjectId: 'exampleorg-shared',

        // BUILD & PUSH
        // dockerRegistry: 'asia-southeast2-docker.pkg.dev', // default is asia-southeast2-docker.pkg.dev (if not set)
        useEnvDockerfile: 'true',
        dockerBuildArgs: '--build-arg environment=staging', // default is '' (if not set)
        registryNamespace: 'exampleorg-shared/exampleorg',
        imageTag: "${env.BUILD_NUMBER}",
        gcrServiceAccount: 'artifact-registry@exampleorg-shared.iam.gserviceaccount.com',

        // DEPLOYMENT
        deployTo: 'gcp_cloud_run_kustomize',
        deployGcpProjectId: 'exampleorg-staging',
        deployServiceAccount: 'cloud-run@exampleorg-staging.iam.gserviceaccount.com',
        imagePlaceholder: 'REPLACE_BUILD_JENKINS',

        // FINAL OPTIONS
        alwaysRemoveImage: 'true'
)
```
