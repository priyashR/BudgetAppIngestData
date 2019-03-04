pipeline {
  agent any
  stages {
    stage('Initialize') {
      parallel {
        stage('Initialize') {
          steps {
            sh '''echo "PATH = ${PATH}"
echo "M2_HOME = ${M2_HOME}"'''
          }
        }
        stage('listDir') {
          steps {
            sh 'ls'
          }
        }
      }
    }
    stage('Build') {
      steps {
        sh '/usr/bin/mvn install'
      }
    }
  }
}