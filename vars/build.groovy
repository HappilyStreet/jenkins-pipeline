def buildStage() {
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
    }
}
return this