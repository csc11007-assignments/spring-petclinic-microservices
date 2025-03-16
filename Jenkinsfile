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
                    def jacocoFiles = []

                    for (service in affectedServices) {
                        echo "Testing service: ${service} on ${env.NODE_NAME}"
                        dir(service) {
                            timeout(time: 10, unit: 'MINUTES') {
                                retry(3) {
                                    sh 'mvn clean test'
                                }
                            }
                            sh 'mvn jacoco:report'
                            jacocoFiles << "${service}/target/jacoco.exec"
                        }
                    }
                    def mergedExec = "merged-jacoco.exec"
                    sh "mvn jacoco:merge -Djacoco.destfile=${mergedExec} -Djacoco.datafiles=${jacocoFiles.join(',')}"
                    sh "mvn jacoco:report -Djacoco.datafile=${mergedExec}"
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'

                    script {
                        echo "Generating merged JaCoCo report"
                        jacoco(
                            execPattern: "merged-jacoco.exec",
                            classPattern: "**/target/classes",
                            sourcePattern: "**/src/main/java",
                            exclusionPattern: "**/src/test/**"
                        )
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
