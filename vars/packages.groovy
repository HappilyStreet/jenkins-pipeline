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


            sh '''
            # Создаём и активируем виртуальное окружение
            python3 -m venv venv
            source venv/bin/activate

            # Обновляем pip и ставим зависимости
            pip install --upgrade pip
            pip install -r requirements.txt

            # Линтинг (не фатально при ошибках)
            pylint test/test_service.py || true

            # Запуск тестов с Allure
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