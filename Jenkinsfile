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
        dir(path: '/home/priyash/GITRepos/BudgetAppIngestData/budgetDataIngestion')
        sh 'mvn install'
      }
    }
  }
  environment {
    maven = 'Maven 3.5.4'
  }
}