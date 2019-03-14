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
    stage('containerize') {
      steps {
        sh '''docker -v
docker login -u $docker_user -p $docker_pwd
echo $buildID
pwd
docker build --build-arg JAR_FILE=budgetDataIngestion/target/budgetDataIngestion-0.0.1-SNAPSHOT.jar -t priyash/budget-app-ingestdata:v$buildID .'''
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