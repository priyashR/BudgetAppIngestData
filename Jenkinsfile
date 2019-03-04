pipeline {
  agent any
  stages {
    stage('Initialize') {
      steps {
        sh '''echo "PATH = ${PATH}"
echo "M2_HOME = ${M2_HOME}"'''
      }
    }
  }
  environment {
    maven = 'Maven 3.5.4'
  }
}