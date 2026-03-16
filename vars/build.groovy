def call(serviceDir, imageTag) {
    stage('Build Stage'){
        echo "🔹 Starting Build Stage"

        echo "Clonee repo"
        dir(serviceDir) {
            sh 'git clone https://github.com/HappilyStreet/MyToDoService.git'
        }

        echo "Installin dependensies"
        dir(serviceDir) {
            sh 'git fetch --all'
            sh 'git checkout main'
            sh 'git pull origin main'
        }
        withCredentials[usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]{
            echo "Pull docket image"
            
            dir(serviceDir) {
                echo "Logging in to Docker Registry..."
                sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

                echo "Building Docker image with tag: mytodo-service:${imageTag}"
                sh "docker build -t mytodo-service:${imageTag} ."
            }
        }
        echo "✅ Build Stage completed."
    }
}