
def call(Map params){

    def dockerFilePath=null
    def imageNameParam=null

    if (params != null){
        dockerFilePath = params.dockerfile
        imageNameParam = params.imageName
    }

    def dockerFilePathLocal = './Dockerfile'
    if (dockerFilePath?.trim()){
        dockerFilePathLocal = dockerFilePath
    }

    String imageNameLocal
    if (!imageNameParam?.trim()){
        imageNameLocal = "${imageName()}"
    }else{
        imageNameLocal = imageNameParam
    }
    if ("${env.DOCKER_REGISTRY}" == 'null') {
        error 'Variable DOCKER_REGISTRY is not defined'
    }

    def dockerImageName = "${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}"

    sh "docker build . -f \"${dockerFilePathLocal}\" -t \"${dockerImageName}:${BRANCH_NAME}-${BUILD_ID}\" -t \"${dockerImageName}:${imageTag()}\""
}

def call(String dockerFilePath=null, String imageNameParam=null){

    call dockerfile: dockerFilePath, imageName: imageNameParam

}
