def testStage() {
    stage('Run some tests into kuber') {
        withEnv(["PATH=${env.HOME}/bin:${env.PATH}"]) {
            dir("${serviceDir}/helm") {
                def values = readYaml file: 'values.yaml'
                def servicePort = values.servicePort
                println "servicePort = ${servicePort}"

                echo "Порт: ${servicePort} используется прикладом"

                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    echo "RUN TEST"
                    def response = sh (
                        script: "curl http://82.117.87.172:${servicePort}/health",
                        returnStdout: true
                    ).trim()
                    if(!response.contains("ok")) {
                        error("service down")
                    }
                    else {
                        echo "${responce}"
                    }
                }
            }
        echo "✅ Тесты выполнены"
        }
    }
}
return this