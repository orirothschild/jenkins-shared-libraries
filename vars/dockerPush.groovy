
def call(String imageNameParam=null){

    call imageName: imageNameParam

}

def call(Map params){

    def imageNameParam = null

    if (params != null){
        imageNameParam = params.imageName
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

    def fullCommitId = commitId()
    def shortCommitId = fullCommitId?.toString()?.take(7)
    sh "docker push \"${dockerImageName}:${imageTag()}\""
    sh "docker push \"${dockerImageName}:${fullCommitId}\""
    sh "docker push \"${dockerImageName}:${shortCommitId}\""
}
