def call(serviceDir, imageTag) {
    stage('Push stage') {
        dir(serviceDir) {
            echo "Pushing Docker image to registry..."
            sh 'docker tag mytodo-service:${imageTag} myregistry/mytodo-service:${imageTag}'
            sh 'docker push myregistry/mytodo-service:${imageTag}'
        }
        echo "Pushing Docker image to registry complete"
    }
}