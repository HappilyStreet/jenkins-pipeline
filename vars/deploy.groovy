def deployStage(){
    stage('Deploy via helm'){
        withEnv(["PATH=${env.HOME}/bin:${env.PATH}"]) {
            withCredentials([string(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]){
            echo "KUBECONFIG path is: ${env.KUBECONFIG}"
            sh "kubectl get nodes --kubeconfig ${env.KUBECONFIG}"

            dir(serviceDir){
                sh "pwd"
                sh "ls -l ./helm"
                echo "Deploying to Kubernetes using Helm..."
                sh "helm upgrade --install mytodo ./helm --set image.tag=${imageTag}"
                }
            }
        }
        echo "✅ Deploy Stage completed."
    }
}
return this