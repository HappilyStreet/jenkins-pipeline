def buildStage() {
    dir(serviceDir) {
        withEnv(["PATH=/usr/local/bin:$PATH"]) {
            echo "Logging in to Docker Registry..."
            sh "echo ${env.DOCKER_SECRET} | docker login -u ${env.DOCKER_USER} --password-stdin"

            echo "Building Docker image with tag: mytodo-service:${imageTag}"
            sh "docker build -t mytodo-service:${imageTag} ${serviceDir}"   

            echo "Pushing Docker image to registry..."
            sh "docker tag mytodo-service:${imageTag} mrsunchip/mytodo-service:${imageTag}"
            sh "docker push mrsunchip/mytodo-service:${imageTag}"  

        }

    }
    echo "✅  Builded and pushed to docker hub"
}
return this