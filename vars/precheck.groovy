def precheckStage() {
    stage('Check installed components for work') {
        echo "Docker check"
        dir(serviceDir) {
            def checkdocker = sh(
                script: "docker --version",
                 returnStdout: true
            ).trim()
            if(!checkdocker.contains("version")){
                echo "Docker isnt install, installing"
                sh "sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin"
            }
            print(checkdocker)

            def checkHelm = sh (
                script: "helm version",
                returnStdout: true
            ).trim()
            if(!checkHelm.contains("version")){
                echo "Helm isnt install, installing"
                sh "curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash"
            }
            print(checkHelm)
        }

    }
    echo "✅ Check complete"
}
return this