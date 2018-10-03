
def call(Map params){
    if (!params){
        return call()
    }
    return call(params.dockerfile as String, params.imageName as String)
}

def call(String dockerFilePath=null, String imageNameParam=null){
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

    sh "docker build . -t ${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}:${imageTag()} -f ${dockerFilePathLocal}"
}
