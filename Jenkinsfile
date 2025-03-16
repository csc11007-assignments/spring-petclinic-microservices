pipeline {
    agent { label 'agent-1 || agent-2' }

    stages {
        stage('Detect Changes') {
            steps {
                script {
                    def affectedServices = []
                    def changedFiles = sh(script: 'git diff --name-only HEAD~1', returnStdout: true).trim().split("\n")
                    echo "Changed files: ${changedFiles}"

                    for (file in changedFiles) {
                        if (file.startsWith("spring-petclinic-") && file.split("/").size() > 1) {
                            def service = file.split("/")[0]
                            if (!affectedServices.contains(service)) {
                                affectedServices << service
                            }
                        }
                    }

                    if (affectedServices.isEmpty()) {
                        echo "No relevant service changes detected. Skipping pipeline."
                        currentBuild.result = 'SUCCESS'
                        return
                    }

                    echo "Affected services: ${affectedServices}"
                    env.AFFECTED_SERVICES = affectedServices.join(',')
                }
            }
        }

        stage('Test and Coverage') {
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            steps {
                script {
                    def affectedServices = env.AFFECTED_SERVICES.split(',')
                    
                    for (service in affectedServices) {
                        echo "Testing service: ${service} on ${env.NODE_NAME}"
                        dir(service) {
                            timeout(time: 10, unit: 'MINUTES') {
                                retry(3) {
                                    sh 'mvn clean test'
                                }
                            }
                            sh 'mvn jacoco:report'
                        }
                    }
                    
                    if (affectedServices.size() > 1) {
                        sh 'mvn jacoco:merge -Djacoco.destFile=target/combined-jacoco.exec -DskipTests'
                        sh 'mvn jacoco:report -Djacoco.dataFile=target/combined-jacoco.exec -DskipTests'
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'

                    script {
                        def affectedServices = env.AFFECTED_SERVICES.split(',')
                        
                        def reportInfo
                        
                        if (affectedServices.size() > 1) {
                            echo "Generating combined JaCoCo report for all affected services"
                            reportInfo = [
                                execPattern: 'target/combined-jacoco.exec',
                                classPattern: '**/target/classes',
                                sourcePattern: '**/src/main/java',
                                exclusionPattern: '**/src/test/**',
                                changeBuildStatus: true,
                                name: 'Combined Coverage Report'
                            ]
                        } else if (affectedServices.size() == 1) {
                            def service = affectedServices[0]
                            echo "Generating JaCoCo report for: ${service}"
                            reportInfo = [
                                execPattern: "${service}/target/jacoco.exec",
                                classPattern: "${service}/target/classes",
                                sourcePattern: "${service}/src/main/java",
                                exclusionPattern: "${service}/src/test/**",
                                changeBuildStatus: true,
                                name: service
                            ]
                        }
                        if (reportInfo) {
                            jacoco(reportInfo)
                        }
                    }
                }
            }
        }

        stage('Build') {
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            steps {
                script {
                    def affectedServices = env.AFFECTED_SERVICES.split(',')
                    for (service in affectedServices) {
                        echo "Building service: ${service} on ${env.NODE_NAME}"
                        dir(service) {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed!"
        }
        always {
            echo "Pipeline completed, cleaning workspace..."
            cleanWs()
        }
    }
}
