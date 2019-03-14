pipeline {
  agent any
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
  }
  tools {
    jdk 'jdk8'
    maven 'maven3'
  }
  environment {
    buildID = "${env.BUILD_ID}"
  }
}