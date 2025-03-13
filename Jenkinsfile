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

                    echo "Affected services: ${affectedServices}"
                    env.AFFECTED_SERVICES = affectedServices.join(',') // Lưu danh sách cho các stage sau
                }
            }
        }

        stage('Test and Coverage') {
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            parallel {
                stage('Test on agent-1') {
                    agent { label 'agent-1' }
                    steps {
                        script {
                            def affectedServices = env.AFFECTED_SERVICES.split(',')
                            for (int i = 0; i < affectedServices.size(); i++) {
                                if (i % 2 == 0) { // Chia task: agent-1 xử lý service chẵn
                                    def service = affectedServices[i]
                                    echo "Testing service: ${service} on agent-1"
                                    dir(service) {
                                        sh 'mvn clean test'
                                        sh 'mvn jacoco:report'
                                    }
                                }
                            }
                        }
                    }
                }
                
                stage('Test on agent-2') {
                    agent { label 'agent-2' }
                    steps {
                        script {
                            def affectedServices = env.AFFECTED_SERVICES.split(',')
                            for (int i = 0; i < affectedServices.size(); i++) {
                                if (i % 2 != 0) { // Agent-2 xử lý service lẻ
                                    def service = affectedServices[i]
                                    echo "Testing service: ${service} on agent-2"
                                    dir(service) {
                                        sh 'mvn clean test'
                                        sh 'mvn jacoco:report'
                                    }
                                }
                            }
                        }
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build') {
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            parallel {
                stage('Build on agent-1') {
                    agent { label 'agent-1' }
                    steps {
                        script {
                            def affectedServices = env.AFFECTED_SERVICES.split(',')
                            for (int i = 0; i < affectedServices.size(); i++) {
                                if (i % 2 == 0) {
                                    def service = affectedServices[i]
                                    echo "Building service: ${service} on agent-1"
                                    dir(service) {
                                        sh 'mvn clean package -DskipTests'
                                    }
                                }
                            }
                        }
                    }
                }

                stage('Build on agent-2') {
                    agent { label 'agent-2' }
                    steps {
                        script {
                            def affectedServices = env.AFFECTED_SERVICES.split(',')
                            for (int i = 0; i < affectedServices.size(); i++) {
                                if (i % 2 != 0) {
                                    def service = affectedServices[i]
                                    echo "Building service: ${service} on agent-2"
                                    dir(service) {
                                        sh 'mvn clean package -DskipTests'
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
