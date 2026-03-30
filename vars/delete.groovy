def deleteStage() {
    stage('Delete service') {
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]){
            withEnv(["PATH=${env.HOME}/bin:${env.PATH}"]){
                dir(serviceDir){
                    echo "Deploying to Kubernetes using Helm..."
                    sh "helm uninstall mytodo -n default"
                }
                def deleteResult = sh(
                    script: "kubectl get deploy",
                    returnStdout: true
                ).trim()
                if(deleteResult.contains("mytodoservice-deployment")){
                    error("Service still working")
                }
                echo "Service war removed"
            }
        }
    }
}
return this