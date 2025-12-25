pipeline {
    agent any
    
    triggers {
        pollSCM('H/10 * * * *')
    }
    
    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/sridhar1511/RevCart-P1-Final.git'
                    ]]
                ])
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
                echo '🚀 Deploying RevCart application...'
                script {
                    sh '''
                        docker-compose -p RevCart-Mono down --remove-orphans || true
                        docker rm -f RevCart-Mono-mysql RevCart-Mono-mongodb RevCart-Mono-backend RevCart-Mono-frontend || true
                        docker rm -f RevCart-Mono-mysql-1 RevCart-Mono-mongodb-1 RevCart-Mono-backend-1 RevCart-Mono-frontend-1 || true
                        docker container prune -f || true
                        docker-compose -p RevCart-Mono up -d --build --force-recreate
                    '''
                }
                echo '✅ Deployment initiated!'
            }
        }
        
        stage('Container Status Check') {
            steps {
                echo '🔍 Checking container status...'
                script {
                    sleep(30) // Wait for containers to start
                    
                    def containers = ['mysql', 'mongodb', 'backend', 'frontend']
                    def allHealthy = true
                    
                    containers.each { container ->
                        def status = sh(script: "docker-compose -p RevCart-Mono ps -q ${container} | xargs docker inspect --format='{{.State.Status}}'", returnStdout: true).trim()
                        if (status == 'running') {
                            echo "✅ ${container}: Running"
                        } else {
                            echo "❌ ${container}: ${status}"
                            allHealthy = false
                        }
                    }
                    
                    if (allHealthy) {
                        echo '🎉 All RevCart containers are running successfully!'
                        echo '📍 Access URLs:'
                        echo '   Frontend: http://localhost:4200'
                        echo '   Backend: http://localhost:8081/api'
                        echo '   MySQL: localhost:3307'
                        echo '   MongoDB: localhost:27017'
                    } else {
                        error('❌ Some containers failed to start properly')
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
        }
        success {
            echo '🎉 RevCart CI Pipeline executed successfully!'
            echo 'Application is validated and ready for deployment'
        }
        failure {
            echo '❌ Pipeline failed. Check logs for details.'
        }
    }
}