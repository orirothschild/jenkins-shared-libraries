
def call(Map params){
    call(params.dockerfile as String, params.imageName as String)
}

def call(String dockerFilePath=null, String imageName=null){
    def dockerFilePathLocal = '.'
    if (dockerFilePath?.trim()){
        dockerFilePathLocal = dockerFilePath
    }
    String imageNameLocal
    if (!imageName?.trim()){
        imageNameLocal = "${JOB_NAME}".split('/')[0]
    }else{
        imageNameLocal = imageName
    }
    def imageTag = "${BRANCH_NAME}-${BUILD_ID}"

    sh "docker build . -t ${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}:${imageTag} -f ${dockerFilePathLocal}"
}
