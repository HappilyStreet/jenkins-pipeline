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

            def checkkubectl = sh (
                script: "",
                returnStdout: true
            ).trim()
            if(!checkkubectl.contains("/usr/bin/kubectl")){
                echo "Kubectl isnt install, installing"
                sh "mkdir -p \$HOME/bin && curl -LO https://dl.k8s.io/release/\$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl && chmod +x kubectl && mv kubectl \$HOME/bin/ && export PATH=\$HOME/bin:\$PATH && kubectl version --client"
            }
        }

    }
    echo "✅ Check complete"
}
return this