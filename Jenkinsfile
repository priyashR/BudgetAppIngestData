pipeline {
  agent any

    environment {
        buildID = ${env.BUILD_ID}
    }

  stages {
    stage('Initialize') {
      steps {
        sh 'echo "PATH = ${PATH}"'
        sh 'echo "M2_HOME = ${M2_HOME}"'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -f budgetDataIngestion/pom.xml install'
      }
    }
    stage('containerize') {
      steps {
        sh '''docker -v
docker login -u $docker_user -p $docker_pwd
echo "Running $buildID"'''
      }
    }
  }
  tools {
    jdk 'jdk8'
    maven 'maven3'
  }
}
