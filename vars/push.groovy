def pushStage() {
    stage('Push stage') {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]){
            dir(serviceDir) {
                echo "Pushing Docker image to registry..."
                sh "docker tag mytodo-service:${imageTag} myregistry/mytodo-service:${imageTag}"
                sh "docker push myregistry/mytodo-service:${imageTag}"
            }
        }
        echo "Pushing Docker image to registry complete"
    }
}
return this