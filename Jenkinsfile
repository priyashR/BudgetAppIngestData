pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('Initialize') {
      steps {
        sh '''echo "PATH = ${PATH}"
echo "M2_HOME = ${M2_HOME}"'''
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -f budgetDataIngestion/pom.xml install'
      }
    }
    stage('') {
      steps {
        sh 'docker.build "priyash/test:$BUILD_NUMBER"'
      }
    }
  }
}