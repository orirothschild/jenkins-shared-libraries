
def call(String imageNameParam=null){

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

    sh "docker push \"${dockerImageName}:${imageTag()}\""
    sh "docker push \"${dockerImageName}:${commitId()}\""

}
