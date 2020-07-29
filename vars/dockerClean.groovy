def call(Map params) {

    def imageNameParam = null

    if (params != null) {
        imageNameParam = params.imageName
    }

    String imageNameLocal
    if (!imageNameParam?.trim()) {
        imageNameLocal = "${imageName()}"
    } else {
        imageNameLocal = imageNameParam
    }
    if ("${env.DOCKER_REGISTRY}" == 'null') {
        error 'Variable DOCKER_REGISTRY is not defined'
    }

    def dockerImageName = "${env.DOCKER_REGISTRY}/bilderlings/${imageNameLocal}"

    sh "docker image rm \"${dockerImageName}:${BRANCH_NAME}-${BUILD_ID}\" \"${dockerImageName}:${imageTag()}\""
}
