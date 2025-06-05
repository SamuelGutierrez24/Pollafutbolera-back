pipeline {
    agent any

    environment {
        GRADLE_OPTS = '-Dorg.gradle.jvmargs="-Xmx2g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8" -Dorg.gradle.daemon=false -Dorg.gradle.workers.max=2'
        JAVA_TOOL_OPTIONS = '-Djava.awt.headless=true'
    }

    options {
        timeout(time: 30, unit: 'MINUTES') // Overall pipeline timeout
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Starting checkout...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Starting build...'
                sh './gradlew clean build -x test --no-daemon'
            }
        }

        stage('Test') {
            options {
                timeout(time: 15, unit: 'MINUTES')
            }
            steps {
                echo 'Starting tests...'
                sh './gradlew test --max-workers=2 --no-daemon --stacktrace --info --tests="*" -Dorg.gradle.workers.max=2 -PmaxParallelForks=2'
            }
            post {
                always {
                    junit '**/build/test-results/**/*.xml'
                }
                failure {
                    echo 'Tests failed. Check the logs for details.'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for details.'
        }
        always {
            cleanWs()
        }
    }
}