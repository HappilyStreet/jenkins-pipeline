def testStage() {
    stage('Run some tests into kuber') {
        withEnv(["PATH=/usr/local/bin:/opt/homebrew/bin:$PATH"]) {
            def serviceport = sh (
                script: "helm get values mytodo -n default -o json | jq -r '.service.port'"
                returnStdout: true
            ).trim
            echo "Порт: ${serviceport} используется прикладом"
        }
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
            echo "RUN TEST"
            def response = sh (
            script: "curl http:82.117.87.172:${servicePort}/health"
            returnStdout: true
            ).trim
            if(!response.contains("ok")) {
                error("service down")
            }
        }
    }
}
return this