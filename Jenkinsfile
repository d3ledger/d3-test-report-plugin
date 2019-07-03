pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
    }
    agent {
        docker {
            label 'd3-build-agent'
            image 'openjdk:8-jdk-alpine'
        }
    }
    stages {
        stage('Build') {
            steps {
                script {
                    sh "./gradlew build --info"
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    sh "./gradlew test --info"
                }
            }
        }
        stage('Build artifacts') {
            steps {
                script {
                    if (env.BRANCH_NAME ==~ /(master)/) {
                        withCredentials([usernamePassword(credentialsId: 'soramitsu-gradle', passwordVariable: 'password', usernameVariable: 'username')]) {
                            sh "./gradlew publishPlugins -Pgradle.publish.key=${username} -Pgradle.publish.secret=${password}"
                        }
                   }
                }
            }
        }
    }
    post {
        cleanup {
            cleanWs()
        }
    }
}
