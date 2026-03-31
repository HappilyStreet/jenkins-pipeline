def packagesStage() {
    echo "🔹 Starting Pakage Stage"
    echo "Cloning repo"

    withEnv(["PATH=/usr/local/bin:$PATH"]) {
        dir(serviceDir) {
            if(fileExists(".git")) {
                echo "✅ Repo exists, pulling latest changes"

                sh "git reset --hard && git clean -fd"
                sh "git pull origin main"
            }
            else {
                echo "🔹Repo didnt exist and will be clone"
                sh "git clone https://github.com/HappilyStreet/MyToDoService.git ."
            }

            echo "Check and install dependensies"


            sh 'rm -rf venv'
            sh 'python3 -m venv venv'
            sh './venv/bin/pip install --upgrade pip'
            sh './venv/bin/pip install -r requirements.txt'
            
            // Линтинг и тесты через полный путь
            sh './venv/bin/pylint test/test_service.py || true'
            sh './venv/bin/pytest --alluredir=allure-results'

            allure([
                includeProperties: false,
                jdk: '',
                results: [[path: "${serviceDir}/allure-results"]]
            ])
        }
    }
    echo "✅ Checkout complete and tests complete"
}
return this