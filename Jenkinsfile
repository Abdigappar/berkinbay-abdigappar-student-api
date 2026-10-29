// Jenkins pipeline - Berkinbay Abdigappar (ID 37409)
// Job name: Berkinbay_Abdigappar_Student_API
pipeline {
    agent any

    tools {
        // Name of the JDK configured in Manage Jenkins -> Tools
        jdk 'jdk17'
    }

    environment {
        STUDENT_NAME = 'Berkinbay Abdigappar'
        STUDENT_ID   = '37409'
        IMAGE_NAME   = 'berkinbay-abdigappar-student-api'
        IMAGE_TAG    = 'v1.0-berkinbay-abdigappar'
    }

    triggers {
        // Check GitHub for new commits every 2 minutes (works without a public Jenkins URL)
        pollSCM('H/2 * * * *')
    }

    options {
        skipDefaultCheckout(true)
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Pipeline of ${STUDENT_NAME} (ID ${STUDENT_ID}) - build #${BUILD_NUMBER}"
                checkout scm
                sh 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                echo "Compiling the Spring Boot application of ${STUDENT_NAME}"
                sh 'chmod +x gradlew'
                sh './gradlew --no-daemon clean assemble'
            }
        }

        stage('Test') {
            steps {
                echo "Running automated tests for ${STUDENT_NAME}"
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image ${IMAGE_NAME}:${IMAGE_TAG}"
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:build-${BUILD_NUMBER} .'
            }
        }

        stage('Verify') {
            steps {
                sh '''
                    docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} > /dev/null
                    echo "Image ${IMAGE_NAME}:${IMAGE_TAG} exists"
                    docker images ${IMAGE_NAME}
                '''
            }
        }
    }

    post {
        success {
            echo "SUCCESS: ${STUDENT_NAME} - image ${IMAGE_NAME}:${IMAGE_TAG} is built and verified"
        }
        failure {
            echo "FAILURE: pipeline of ${STUDENT_NAME} stopped. Check the stage marked red in the console output."
        }
    }
}
