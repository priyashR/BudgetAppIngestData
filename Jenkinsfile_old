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
    stage('changeDir') {
      steps {
        dir(path: 'budgetDataIngestion')
      }
    }
  }
  environment {
    maven = 'Maven 3.5.4'
  }
}