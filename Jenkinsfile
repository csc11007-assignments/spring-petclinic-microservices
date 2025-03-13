pipeline {
    agent none  // Sử dụng agent none để chỉ định agent riêng cho từng stage

    stages {
        stage('Detect Changes') {
            agent { label 'agent-1' }  // Chạy trên agent-1
            steps {
                script {
                    def affectedServices = [] // Danh sách các service bị thay đổi
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
                    env.AFFECTED_SERVICES = affectedServices.join(',') // Lưu danh sách thành chuỗi cho các stage sau
                }
            }
        }

        stage('Test and Coverage') {
            agent { label 'agent-1' }  // Chạy trên agent-1
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            steps {
                script {
                    def affectedServices = env.AFFECTED_SERVICES.split(',')
                    for (service in affectedServices) {
                        echo "Testing service: ${service} on ${env.NODE_NAME}"
                        dir(service) {
                            // Chạy test với JaCoCo
                            sh 'mvn clean test'
                            // Tạo báo cáo JaCoCo
                            sh 'mvn jacoco:report'
                        }
                    }
                }
            }
            post {
                always {
                    // Báo cáo kết quả test chỉ của các service đã chạy test
                    junit '**/target/surefire-reports/*.xml'

                    script {
                        def affectedServices = env.AFFECTED_SERVICES.split(',')
                        for (service in affectedServices) {
                            echo "Generating JaCoCo report for: ${service}"
                            jacoco(
                                execPattern: "${service}/target/jacoco.exec", // Chỉ lấy exec của service test
                                classPattern: "${service}/target/classes",
                                sourcePattern: "${service}/src/main/java",
                                exclusionPattern: "${service}/src/test/**",
                                minimumLineCoverage: '70', // Yêu cầu tối thiểu 70% coverage
                                changeBuildStatus: true // Thất bại nếu không đạt ngưỡng
                            )
                        }
                    }
                }
            }
        }

        stage('Build') {
            agent { label 'agent-1' }  // Chạy trên agent-1
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
