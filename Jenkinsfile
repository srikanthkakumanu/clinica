pipeline {
    agent any
    stages {
        stage("Build") {
            steps { 
                echo "Running Clinica Build Automation"
                sh './gradlew build --no-daemon'
                archiveArtifacts artifacts: 'build/libs/clinica-0.0.1.jar'

            }
            post {
                always {
                    echo "========always========"
                }
                success {
                    echo "========Clinica Build executed successfully========"
                }
                failure {
                    echo "========Clinica Build execution failed========"
                }
            }
        }
    }
 }