def testStage() {
    echo "🔹 Running tests after deploy"

    dir("${serviceDir}/helm") {
        sh '''
            python3 -m venv venv
            venv/bin/pip install -r requirements.txt
            venv/bin/pytest tests/test_service.py --alluredir=allure-results
        '''

        allure([
            includeProperties: false,
            jdk: '',
            results: [[path: "${serviceDir}/helm/allure-results"]]
        ])
    }
}
return this