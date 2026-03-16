def call() {
    stage('Build Stage'){
        echo "🔹 Starting Build Stage"

        echo "Clonee repo"
        dir('..') {
            sh 'git clone https://github.com/HappilyStreet/MyToDoService.git'
        }

        echo"Install dependensies"
        dir(serviceDir) {
            sh 'git fetch --all'
            sh 'git checkout main'
            sh 'git pull origin main'
        }

        echo"Pull docket image"
        dir(serviceDir) {
            def imageTag = env.BUILD_NUMBER
            echo "Building Docker image with tag: mytodo-service:${imageTag}"
            sh "docker build -t mytodo-service:${imageTag} ."
        }

        echo "✅ Build Stage completed."
    }
}