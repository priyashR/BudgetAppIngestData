pipeline {
  agent any
    tools {
        jdk 'jdk8'
        maven 'maven3'
        docker 'docker'
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
        sh 'docker -version'
      }
    }
  }
}
