def buildStage() {
    stage('Build Stage'){
        echo "🔹 Starting Build Stage"

        echo "Clone repo"
        dir(serviceDir) {
            sh 'git clone https://github.com/HappilyStreet/MyToDoService.git'
        }

        echo "Installing dependensies"
        dir(serviceDir) {
            sh 'git fetch --all'
            sh 'git checkout main'
            sh 'git pull origin main'
        }
        withEnv(["PATH=/usr/local/bin:$PATH"]) {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]){
                echo "Pull docket image"
                
                dir(serviceDir) {
                    echo "Logging in to Docker Registry..."
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

                    echo "Building Docker image with tag: mytodo-service:${imageTag}"
                    sh "docker build -t mytodo-service:${imageTag} ."
                }
            }
        }
        echo "✅ Build Stage completed."
    }
}
return this