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
                # Установим pip, если нет
                if ! command -v pip3 >/dev/null 2>&1; then
                    echo "Installing pip..."
                    curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
                    python3 get-pip.py --user
                fi

                # Создаём виртуальное окружение
                python3 -m venv venv
                . venv/bin/activate

                # Обновляем pip
                pip install --upgrade pip

                # Устанавливаем зависимости из requirements.txt
                if [ -f requirements.txt ]; then
                    pip install -r requirements.txt
                else
                    # Если requirements.txt нет, ставим стандартный набор
                    pip install pytest pytest-cov allure-pytest pylint flask sqlalchemy requests
                fi

                # Проверка кода
                pylint **/*.py || true

                # Запуск тестов
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