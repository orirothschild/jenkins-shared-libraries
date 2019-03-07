
def call(String imageNameParam=null){

    call imageName: imageNameParam

}

def call(Map params){

    String imageNameParam=null

    if (params != null){
        imageNameParam = params.imageName
    }

    String imageNameLocal
    if (!imageNameParam?.trim()){
        imageNameLocal = "${imageName()}"
    }else{
        imageNameLocal = imageNameParam
    }

    if (!env.DOCKER_REGISTRY) {
        currentBuild.result = 'FAILURE'
        error 'Variable DOCKER_REGISTRY is not defined'
    }

    def dockerImageName = "${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}"

    sh "docker tag \"${dockerImageName}:${imageTag()}\" \"${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}:latest\""
    sh "docker push \"${dockerImageName}:latest\""
}