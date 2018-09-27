
def call(String dockerFilePath='./Dockerfile'){

    def imageName = "${JOB_NAME}".split('/')[0]
    def imageTag = "${BRANCH_NAME}-${BUILD_ID}"
    if (!dockerFilePath?.trim()){
        error "Invalid docker file path: ${dockerFilePath}"
    }
    sh "docker build . -t ${env.DOCKER_REGISTRY}/bilderlings/${imageName}:${imageTag} -f ${dockerFilePath}"

}
