pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-south-1'
        ECR_REGISTRY = '411653576368.dkr.ecr.ap-south-1.amazonaws.com'
        ECR_REPOSITORY = 'employee-service'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        IMAGE_URI = "${ECR_REGISTRY}/${ECR_REPOSITORY}:build-${BUILD_NUMBER}"

        DEPLOYMENT_NAME = 'employee-service'
        CONTAINER_NAME = 'employee-service'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvnw.cmd test'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_URI% .'
            }
        }

        stage('Login to ECR') {
            steps {
                bat 'aws ecr get-login-password --region %AWS_REGION% | docker login --username AWS --password-stdin %ECR_REGISTRY%'
            }
        }

        stage('Push Image to ECR') {
            steps {
                bat 'docker push %IMAGE_URI%'
            }
        }

        stage('Deploy to EKS') {
            steps {
                bat 'kubectl set image deployment/%DEPLOYMENT_NAME% %CONTAINER_NAME%=%IMAGE_URI%'

                bat 'kubectl rollout status deployment/%DEPLOYMENT_NAME% --timeout=5m'
            }
        }

        stage('Verify Deployment') {
            steps {
                bat 'kubectl get pods'
                bat 'kubectl get deployment %DEPLOYMENT_NAME%'
                bat 'kubectl get service %DEPLOYMENT_NAME%'
                bat 'kubectl get ingress employee-ingress'
            }
        }
    }

    post {
        success {
            echo "Deployment successful: ${IMAGE_URI}"
        }

        failure {
            echo "Pipeline failed. Check the failed stage."
        }
    }
}
