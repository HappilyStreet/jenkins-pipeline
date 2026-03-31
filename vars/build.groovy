def buildStage() {
    dir(serviceDir) {
        withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASS")]) {
        echo "Logging in to Docker Registry..."
        sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"

        echo "Building Docker image with tag: mytodo-service:${imageTag}"
        sh "docker build -t mytodo-service:${imageTag} ${serviceDir}"     

        }
    }
    echo "✅  Builded and pushed to docker hub"
}