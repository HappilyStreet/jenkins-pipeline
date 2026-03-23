def buildStage() {
    stage("Checkout repository and install dependensies"){
        echo "🔹 Starting Build Stage"
        echo "Cloning repo" 

        withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASS")]) {
            withEnv(["PATH=/usr/local/bin:$PATH"]) {
                dir(serviceDir) {
                    if(fileExists(".git")) {
                        echo "✅ Repo exists, pulling latest changes"
                        sh "git reset --hard && git clean -fd"
                        sh "git pull origin main"
                    }
                    else {
                        echo "🔹Repo didnt exist and will be clone"
                        sh "git clone https://github.com/HappilyStreet/MyToDoService.git ."
                    }

                    docker.image('python:3.11-slim').inside {
                        sh '''
                            pip install --no-cache-dir flake8
                            flake8 .
                        '''
                    }

                    echo "✅ Checkout complete"
                    echo "✅ linter check complete"     

                    echo "Logging in to Docker Registry..."
                    sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"

                    echo "Building Docker image with tag: mytodo-service:${imageTag}"
                    sh "docker build -t mytodo-service:${imageTag} ${serviceDir}"
                }
            }
        }
    }
    echo "✅  Image was builded"
    echo "✅ Build Stage completed"
}
return this