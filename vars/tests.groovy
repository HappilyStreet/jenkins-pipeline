def testStage() {
    echo "🔹 Running tests фаеук вуздщн"
    dir("${serviceDir}/helm") {
        sh '''
        . venv/bin/activate
        pytest tests/test_service.py --alluredir=allure-results
        '''

        allure([
            includeProperties: false,
            jdk: '',
            results: [[path: "${serviceDir}/allure-results"]]
        ])
    }
}
return this