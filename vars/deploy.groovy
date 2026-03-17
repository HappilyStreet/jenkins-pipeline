def deployStage(){
    stage('Deploy via helm'){
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]){
            withEnv(["PATH=/usr/local/bin:/opt/homebrew/bin:$PATH"]) {
            echo "KUBECONFIG path is: ${env.KUBECONFIG}"
            sh "kubectl get nodes --kubeconfig ${env.KUBECONFIG}"

            dir('app'){
                echo "pwd"
                echo "Deploying to Kubernetes using Helm..."
                sh "helm upgrade --install mytodo ./helm --set image.tag=${imageTag}"
                }
            }
        }
        echo "✅ Deploy Stage completed."
    }
}
return this