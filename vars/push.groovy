def pushStage() {
    stage('Push stage') {
         withEnv(["PATH=/usr/local/bin:$PATH"]) {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]){
                dir(servieDir) {
                    echo "Pushing Docker image to registry..."
                    sh "docker tag mytodo-service:${imageTag} mrsunchip/mytodo-service:${imageTag}"
                    sh "docker push mrsunchip/mytodo-service:${imageTag}"
                }
            }
         }

        echo "Pushing Docker image to registry complete"
    }
}
return this