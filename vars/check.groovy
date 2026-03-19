def testStage() {
    stage('Run some tests into kuber') {
        withEnv(["PATH=/usr/local/bin:/opt/homebrew/bin:$PATH"]) {
            def checkJQ = sh (
                script: "which jq",
                returnStdout: true
            ).trim()
            if(!checkJQ.contains("jq")){
                script: "apt update && apt install -y jq"
            }
            else {
                echo "✅ jq установлен можно выполнять проверки"
            }
            def servicePort = sh (
                script: "helm get values mytodo -n default -o json | jq -r '.service.port'",
                returnStdout: true
            ).trim()
            echo "Порт: ${servicePort} используется прикладом"
            withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                echo "RUN TEST"
                def response = sh (
                    script: "curl http:82.117.87.172:${servicePort}/health",
                    returnStdout: true
                ).trim()
                if(!response.contains("ok")) {
                    error("service down")
                }
            }
        }
        echo "✅ Тесты выполнены"
    }
}
return this