def testStage() {
    echo "🔹 Running tests after deploy"

    dir(serviceDir) {
        sh '''
            pwd
            python3 -m venv venv
            venv/bin/pip install -r  ${serviceDir}/requirements.txt
            venv/bin/pytest test/test_service.py --alluredir=allure-results
        '''

        allure([
            includeProperties: false,
            jdk: '',
            results: [[path: 'allure-results']]
        ])
    }
}
return this