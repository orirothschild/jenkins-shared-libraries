
def call(String imageNameParam=null){

    String imageNameLocal
    if (!imageNameParam?.trim()){
        imageNameLocal = "${imageName()}"
    }else{
        imageNameLocal = imageNameParam
    }

    if (!env.DOCKER_REGISTRY) {
        error 'Variable DOCKER_REGISTRY is not defined'
    }

    def dockerImageName = "${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}"

    sh "docker tag \"${dockerImageName}:${imageTag()}\" \"${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}:latest\""
    sh "docker push \"${dockerImageName}:latest\""

}
