def call(serviceDir, imageTag){
    stage('Deploy via helm'){
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]){
            
        echo "KUBECONFIG path is: ${env.KUBECONFIG}"
        sh "kubectl get nodes --kubeconfig ${env.KUBECONFIG}"

        dir("${serviceDir}/MyToDoService"){
            echo "Deploying to Kubernetes using Helm..."
            sh 'helm upgrade --install mytodo ./helm-chart --set image.tag=${imageTag}'
            }
        }
        echo "✅ Deploy Stage completed."
    }
}