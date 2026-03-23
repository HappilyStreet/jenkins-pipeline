def buildStage() {
    stage("Checkout repository and install dependensies"){
        echo "🔹 Starting Build Stage"
        echo "Cloning repo"
        dir(serviceDir) {
            if(fileExists(".git")) {
                echo "✅ Repo exist"
                sh "git pull https://github.com/HappilyStreet/MyToDoService.git"
            }
            else {
                echo "🔹Repo didnt exist and will be clone"
                sh "git clone https://github.com/HappilyStreet/MyToDoService.git ."
            }
        }
        echo "Installing dependensies"
        dir(serviceDir) {
            sh "git fetch --all"
            sh "git checkout main"
            sh "git pull origin main"
        }
    }
    echo "✅ Checkout complete"
    stage("Linter check") {
        docker {
            image 'python:3.11-slim'
        }
        steps{
            sh '''
                pip install --no-cache-dir flake8
                flake8 .
            '''
        }
    }
    echo "✅ linter check complete"
    stage("Build image") {
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