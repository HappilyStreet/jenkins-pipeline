def deployStage(){
    withEnv(["PATH=${env.HOME}/bin:${env.PATH}"]) {
        echo "🔹 BUILD=install → fetching latest image tag from Docker Hub"
        def repo = 'mrsunchip/mytodo-service'

        def response = sh (
            script: "curl -s 'https://hub.docker.com/v2/repositories/${repo}/tags?page_size=1&ordering=last_updated'",
            returnStdout: true
        ).trim()
    
        def json = readJSON (text: response)
        imageTag = json.results[0].name

        echo "✅ Latest image tag from Docker Hub: ${imageTag}"

        // echo "KUBECONFIG path is: ${env.KUBECONFIG}"
        sh "kubectl get nodes --kubeconfig ${env.KUBE_CONFIG_PATH}"
        dir(serviceDir){
            sh "pwd"
            sh "ls -l ./helm"
            echo "Deploying to Kubernetes using Helm..."
            sh "helm upgrade --install mytodo ./helm --set image.tag=${imageTag}"
        }
    }
echo "✅ Deploy Stage completed."
}
return this