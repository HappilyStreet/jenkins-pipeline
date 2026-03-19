def testStage() {
    stage('Run some tests into kuber') {
        withEnv(["PATH=/usr/local/bin:/opt/homebrew/bin:$PATH"]) {
            dir("${serviceDir}/helm") {
                def values = readYaml file: 'values.yaml'
                def servicePort = values.servicePort
                println "servicePort = ${servicePort}"
            }
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
        echo "✅ Тесты выполнены"
        }
    }
}
return this