
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

    sh "docker push \"${dockerImageName}:${BRANCH_NAME}-${BUILD_ID}\""
    sh "docker push \"${dockerImageName}:${imageTag()}\""
    //if("${BRANCH_NAME}" == 'master') {
        anchore_analyze("${dockerImageName}:${imageTag()}")
    //}
}
