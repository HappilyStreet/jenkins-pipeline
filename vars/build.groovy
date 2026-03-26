def buildStage() {
    stage("Checkout repository and install dependensies"){
        echo "🔹 Starting Build Stage"
        echo "Cloning repo"

        def hasChange = false
        
        withEnv(["PATH=/usr/local/bin:$PATH"]) {
            dir(serviceDir) {
                if(fileExists(".git")) {
                    echo "✅ Repo exists, pulling latest changes"

                    sh "git fetch origin main"

                    def diff = sh(
                        script: "git diff HEAD origin/main",
                        returnStdout: true
                    ).trim()

                    if(diff) {
                        echo "🔄 Changes detected"
                        hasChange = true

                        sh "git reset --hard && git clean -fd"
                        sh "git pull origin main"
                    } else {
                        echo "🔹No changes in repo"
                    }
                } else {
                    echo "🔹Repo didnt exist and will be clone"
                    sh "git clone https://github.com/HappilyStreet/MyToDoService.git ."
                }


            }
            echo "✅ Checkout complete"

            if(hasChange == true) {
                withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASS")]) {
                    echo "Logging in to Docker Registry..."
                    sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"

                    echo "Building Docker image with tag: mytodo-service:${imageTag}"
                    sh "docker build -t mytodo-service:${imageTag} ${serviceDir}"

                }
            } echo "✅ Build Stage completed"
            else {
                echo "🚫 Skipping build — no changes"
            }
        }
    }
    echo "✅  Image was builded"
    return hasChanges
}