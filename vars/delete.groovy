def deleteStage() {
    stage('Delete service') {
        // def kubeconfig = env.KUBE_CONFIG_PATH
        withEnv(["PATH=${env.HOME}/bin:${env.PATH}"]){
            dir(serviceDir){
                
                echo "Uninstall using Helm..."
                sh "helm uninstall mytodo -n default --kubeconfig ${env.KUBE_CONFIG_PATH}"
            }
            def deleteResult = sh(
                script: "kubectl get deploy --kubeconfig ${env.KUBE_CONFIG_PATH}",
                returnStdout: true
            ).trim()
            if(deleteResult.contains("mytodoservice-deployment")){
                error("Service still working")
            }
            echo "Service war removed"
        }
    }
}
return this