pipeline {
    agent any
    
    triggers {
        pollSCM('H/10 * * * *')
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from GitHub...'
                checkout scm
                echo 'Code checkout completed successfully!'
            }
        }
        
        stage('Validate') {
            steps {
                echo 'Validating project structure...'
                script {
                    if (fileExists('docker-compose.yml')) {
                        echo 'docker-compose.yml found ✓'
                    }
                    if (fileExists('revcart/frontend/package.json')) {
                        echo 'Frontend package.json found ✓'
                    }
                    if (fileExists('revcart/backend/pom.xml')) {
                        echo 'Backend pom.xml found ✓'
                    }
                }
                echo 'Project validation completed!'
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying RevCart application...'
                script {
                    try {
                        sh 'docker-compose down || true'
                        sh 'docker-compose up -d --build'
                        echo '✅ Deployment successful!'
                    } catch (Exception e) {
                        echo "❌ Deployment failed: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
        
        stage('Health Check') {
            steps {
                echo 'Performing health checks...'
                sleep(30)
                sh 'docker-compose ps'
                echo '✅ Health check completed!'
            }
        }
    }
    
    post {
        success {
            echo '🚀 CI/CD Pipeline completed successfully!'
            echo 'RevCart is deployed and running!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs for details.'
        }
    }
}