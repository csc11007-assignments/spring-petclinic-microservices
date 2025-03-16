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
                    
                    script {
                        def jacocoFiles = findFiles(glob: '**/target/jacoco.exec')
                        if (jacocoFiles.length > 0) {
                            def jacocoFileList = jacocoFiles.collect { it.path }.join(',')
                            echo "Merging JaCoCo reports for: ${jacocoFileList}"
                            sh "mvn jacoco:merge -Djacoco.destFile=${env.WORKSPACE}/combined-jacoco.exec -Djacoco.fileSet=\"${jacocoFileList}\""
                            sh "mvn jacoco:report -Djacoco.dataFile=${env.WORKSPACE}/combined-jacoco.exec"
                        } else {
                            echo "No JaCoCo execution files found to merge"
                        }
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'

                    script {
                        def affectedServices = env.AFFECTED_SERVICES.split(',')
                        if (!affectedServices.isEmpty() && fileExists("${env.WORKSPACE}/combined-jacoco.exec")) {
                            echo "Generating combined JaCoCo report for affected services"
                            jacoco(
                                execPattern: "${env.WORKSPACE}/combined-jacoco.exec",
                                classPattern: affectedServices.collect { "${it}/target/classes" }.join(','),
                                sourcePattern: affectedServices.collect { "${it}/src/main/java" }.join(','),
                                exclusionPattern: affectedServices.collect { "${it}/src/test/**" }.join(',')
                            )
                        } else {
                            echo "Skipping JaCoCo report generation - no coverage data available"
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
}
