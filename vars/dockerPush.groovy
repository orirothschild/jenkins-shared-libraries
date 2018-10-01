
def call(String imageName=null){
    String imageNameLocal
    if (!imageName?.trim()){
        imageNameLocal = "${JOB_NAME}".split('/')[0]
    }else{
        imageNameLocal = imageName
    }
    def imageTag = "${BRANCH_NAME}-${BUILD_ID}"
    if ("${env.DOCKER_REGISTRY}" == 'null') {
        error 'Variable DOCKER_REGISTRY is not defined'
    }
    sh "docker push ${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}:${imageTag}"

}
