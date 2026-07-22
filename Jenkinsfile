pipeline {
    agent any
    environment {
        DOCKERHUB_CREDS = credentials('dockerhub-creds')
        BACKEND_IMAGE = "rashmi200/spring-backend"
        CLIENT_IMAGE  = "rashmi200/react-client"
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/RASHMIBB23/spring-react-docker-kubernetes.git'
            }
        }

        stage('Build Backend Jar') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t $BACKEND_IMAGE:$BUILD_NUMBER -t $BACKEND_IMAGE:latest ."
                sh "docker build -t $CLIENT_IMAGE:$BUILD_NUMBER -t $CLIENT_IMAGE:latest ./client"
            }
        }

        stage('Docker Push') {
            steps {
                sh "echo $DOCKERHUB_CREDS_PSW | docker login -u $DOCKERHUB_CREDS_USR --password-stdin"
                sh "docker push $BACKEND_IMAGE:$BUILD_NUMBER"
                sh "docker push $BACKEND_IMAGE:latest"
                sh "docker push $CLIENT_IMAGE:$BUILD_NUMBER"
                sh "docker push $CLIENT_IMAGE:latest"
            }
        }

        stage('Deploy MySQL (idempotent)') {
            steps {
                sh "kubectl apply -f mysql-pvc.yaml"
                sh "kubectl apply -f mysql-deployment.yaml"
                sh "kubectl apply -f mysql-service.yaml"
            }
        }

        stage('Deploy Backend & Client') {
            steps {
                sh "kubectl apply -f backend-deployment.yaml"
                sh "kubectl apply -f backend-service.yaml"
                sh "kubectl apply -f client-deployment.yaml"
                sh "kubectl apply -f client-service.yaml"

                sh "kubectl set image deployment/backend backend=$BACKEND_IMAGE:$BUILD_NUMBER"
                sh "kubectl set image deployment/client client=$CLIENT_IMAGE:$BUILD_NUMBER"

                sh "kubectl rollout status deployment/backend"
                sh "kubectl rollout status deployment/client"
            }
        }
    }
    post {
        success { echo '✅ Pipeline completed successfully' }
        failure { echo '❌ Pipeline failed — check console logs' }
    }
}
