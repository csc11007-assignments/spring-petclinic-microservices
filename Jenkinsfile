pipeline {
    agent none  // Không dùng "any", sẽ chỉ định agent ở từng stage
    
    stages {
        stage('Detect Changes') {
            agent { label 'agent-1' }  // Chạy trên agent-1
            steps {
                script {
                    def affectedServices = [] // Danh sách service bị thay đổi
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

                    // Sử dụng Collections.shuffle() để xáo trộn danh sách
                    java.util.Collections.shuffle(affectedServices)

                    echo "Affected services (shuffled): ${affectedServices}"
                    env.AFFECTED_SERVICES = affectedServices.join(',') // Lưu danh sách cho các stage sau
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
                    def parallelStages = [:] // Map để chứa các stage động

                    // Tạo stage động cho từng service
                    affectedServices.eachWithIndex { service, index ->
                        parallelStages["Test ${service} (${index})"] = {
                            stage("Test ${service}") {
                                // Chọn agent dựa trên nhãn chung (Jenkins sẽ tự phân phối)
                                agent { label 'agent-1 || agent-2' }
                                steps {
                                    echo "Testing service: ${service} on ${env.NODE_NAME}"
                                    dir(service) {
                                        sh 'mvn clean test'
                                        sh 'mvn jacoco:report'
                                    }
                                }
                                post {
                                    always {
                                        junit '**/target/surefire-reports/*.xml'
                                    }
                                }
                            }
                        }
                    }

                    // Chạy các stage song song
                    parallel parallelStages
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
                    def parallelStages = [:] // Map để chứa các stage động

                    // Tạo stage động cho từng service
                    affectedServices.eachWithIndex { service, index ->
                        parallelStages["Build ${service} (${index})"] = {
                            stage("Build ${service}") {
                                // Chọn agent dựa trên nhãn chung (Jenkins sẽ tự phân phối)
                                agent { label 'agent-1 || agent-2' }
                                steps {
                                    echo "Building service: ${service} on ${env.NODE_NAME}"
                                    dir(service) {
                                        sh 'mvn clean package -DskipTests'
                                    }
                                }
                            }
                        }
                    }

                    // Chạy các stage song song
                    parallel parallelStages
                }
            }
        }
    }
}
