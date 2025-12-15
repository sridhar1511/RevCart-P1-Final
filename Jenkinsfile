pipeline {
    agent any
    
    triggers {
        pollSCM('H/10 * * * *')
    }
    
    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from GitHub...'
                checkout scm
            }
        }
        
        stage('Build and Deploy') {
            steps {
                script {
                    try {
                        echo 'Building and deploying with Docker Compose...'
                        sh 'docker-compose down || true'
                        sh 'docker-compose up -d --build'
                        echo 'Deployment successful!'
                    } catch (Exception e) {
                        echo "Deployment failed: ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    echo 'Performing health checks...'
                    sleep(30) // Wait for services to start
                    
                    // Check if containers are running
                    sh 'docker-compose ps'
                    
                    // Optional: Add specific health checks
                    echo 'Health check completed'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for details.'
        }
    }
}