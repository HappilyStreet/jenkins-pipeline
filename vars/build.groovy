def buildStage() {
    stage("Checkout repository and install dependensies"){
        echo "🔹 Starting Build Stage"
        echo "Cloning repo"
        docker.image('python:3.11-slim').inside {
            sh '''
                pip install flake8
                flake8 .
            '''
        }
        dir(serviceDir) {
            if(fileExists(".git")) {
                echo "✅ Repo exist"
                sh "git pull https://github.com/HappilyStreet/MyToDoService.git"
            }
            else {
                echo "🔹Repo didnt exist and will be clone"
                sh "git clone https://github.com/HappilyStreet/MyToDoService.git ."
            }

            sh "git fetch --all"
            sh "git checkout main"
            sh "git pull origin main"

            
        }
        echo "✅ Checkout complete"

        dir(serviceDir) {
            sh '''
                pip install --no-cache-dir flake8
                flake8 .
            '''
        }
        echo "✅ linter check complete"

        withEnv(["PATH=/usr/local/bin:$PATH"]) {
            withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASS")]){
                echo "Pull docket image"
                
                dir(serviceDir) {
                    echo "Logging in to Docker Registry..."
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"

                    echo "Building Docker image with tag: mytodo-service:${imageTag}"
                    sh "docker build -t mytodo-service:${imageTag} ."
                }
            }
        }
    }
    echo "✅  Image was builded"
    echo "✅ Build Stage completed"
}
return this