import groovy.transform.Field

@Field
Map colorMap = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'warning']

def call(String channelParameter = null, allureParameter = null){

    call channel: channelParameter, allure: allureParameter

}

def call(Map params){

    def channelParameter = null
    def allureParameter = null
    def blueOceanParameter = true

    if (params != null){
        channelParameter = params.channel
        allureParameter = params.allure
        if (params.blueocean != null){
            blueOceanParameter = params.blueocean.toString()?.toBoolean()
        }
    }

    def result = currentBuild.currentResult
    if (!colorMap.containsKey(result)){
        echo "slackSend is muted. Undefined build status: ${result}"
        return
    }
    def channel = channelParameter?.toString()?.trim()
    def allure = allureParameter?.toString()?.toBoolean()

    def slackParams = [
            color: colorMap.get(result),
            message: generateSlackMessage(allure, blueOceanParameter)
    ]
    if (channel){
        slackParams.channel = "$channel"
    }
    slackSend slackParams

}

@Field
Map resultMessageMap = ['SUCCESS': 'build passed', 'FAILURE': 'build failed', 'UNSTABLE': 'tests failed']

private generateSlackMessage(Boolean allureIsUsed, Boolean blueOcean){
    def result = currentBuild.currentResult
    def resultMessage = resultMessageMap.get(result)
    def imageNameLocal = imageName()
    def message = "${imageNameLocal} branch ${BRANCH_NAME} ${resultMessage}!"
    if (allureIsUsed) {
        message += " <${BUILD_URL}allure/|build-tests>"
    }
    message += getAllureReportsMessage()
    message += blueOcean? getBlueOceanLink(imageNameLocal) : getClassicLink(imageNameLocal)
    message
}

private getAllureReportsMessage(){
    def message = ''
    env.getEnvironment().each { k, v ->
        def key = k.toString()
        def index = key.indexOf('_TESTS_URL')
        if (index != -1){
            def allureJobName = key.substring(0, index)
            message += " <$v|${allureJobName}>"
        }
    }
    message
}

private getBlueOceanLink(imageName){
    def url = "${JENKINS_URL}blue/organizations/jenkins/${imageName}/detail/${BRANCH_NAME}/${BUILD_ID}/pipeline/"
    " (<${url}|${BUILD_ID}>)"
}

private getClassicLink(imageName){
    def url = "${JENKINS_URL}job/${imageName}/job/${BRANCH_NAME}/${BUILD_ID}/"
    " (<${url}|${BUILD_ID}>)"
}
