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

            sh 'python3 -m venv venv'


            sh '''
            . venv/bin/activate
            pip install --upgrade pip
            pip install pytest pytest-cov allure-pytest
            pip install pylint flask sqlalchemy
            '''


            sh '''
            . venv/bin/activate
            pylint **/*.py || true
            '''

            sh '''
            . venv/bin/activate
            pytest --alluredir=allure-results
            '''

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