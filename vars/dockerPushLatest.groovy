
def call(){

    def imageName = "${JOB_NAME}".split('/')[0]
    def imageTag = "${BRANCH_NAME}-${BUILD_ID}"
    sh "docker tag ${env.DOCKER_REGISTRY}/bilderlings/${imageName}:${imageTag} ${env.DOCKER_REGISTRY}/bilderlings/${imageName}:latest"
    sh "docker push ${env.DOCKER_REGISTRY}/bilderlings/${imageName}:${imageTag}"
    sh "docker push ${env.DOCKER_REGISTRY}/bilderlings/${imageName}:latest"

}
