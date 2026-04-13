def pushStage() {
    stage('Push stage') {
         withEnv(["PATH=/usr/local/bin:$PATH"]) {
            dir(serviceDir) {
                echo "Logging in to Docker Registry..."
                sh "echo ${env.DOCKER_SECRET} | docker login -u ${env.USER_DOCKER} --password-stdin"

                echo "Pushing Docker image to registry..."
                sh "docker tag mytodo-service:${imageTag} mrsunchip/mytodo-service:${imageTag}"
                sh "docker push mrsunchip/mytodo-service:${imageTag}"
            }
         }

        echo "Pushing Docker image to registry complete"
    }
}
return this