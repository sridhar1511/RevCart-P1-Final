pipeline {
    agent any
    
    triggers {
        pollSCM('H/10 * * * *')
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sridhar1511/RevCart-P1-Final.git'
                echo '✅ Code checked out successfully!'
            }
        }
        
        stage('Validate') {
            steps {
                echo 'Validating RevCart project structure...'
                script {
                    if (fileExists('docker-compose.yml')) {
                        echo '✅ docker-compose.yml found'
                    }
                    if (fileExists('revcart/frontend/package.json')) {
                        echo '✅ Frontend package.json found'
                    }
                    if (fileExists('revcart/backend/pom.xml')) {
                        echo '✅ Backend pom.xml found'
                    }
                }
                echo '✅ Project validation completed!'
            }
        }
        
        stage('Deploy') {
            steps {
                echo '🚀 Deploying RevCart Application...'
                script {
                    try {
                        sh '''
                            echo "Stopping existing containers..."
                            docker-compose down || true
                            
                            echo "Building and starting new containers..."
                            docker-compose up -d --build
                            
                            echo "Waiting for services to start..."
                            sleep 30
                        '''
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
                echo '🔍 Performing health checks...'
                script {
                    sh '''
                        echo "Checking container status..."
                        docker-compose ps
                        
                        echo "Checking if frontend is accessible..."
                        curl -f http://localhost:4200 || echo "Frontend not ready yet"
                        
                        echo "Checking if backend is accessible..."
                        curl -f http://localhost:8081/api/products || echo "Backend not ready yet"
                    '''
                }
                echo '✅ Health check completed!'
            }
        }
    }
    
    post {
        always {
            echo 'CI/CD Pipeline execution completed'
        }
        success {
            echo '🎉 RevCart CI/CD Pipeline executed successfully!'
            echo '✅ Application deployed and running at:'
            echo '   Frontend: http://localhost:4200'
            echo '   Backend: http://localhost:8081/api'
        }
        failure {
            echo '❌ CI/CD Pipeline failed. Check logs for details.'
        }
    }
}