import groovy.transform.Field

@Field
Map colorMap = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'warning']

def call(Map params){
    if (!params){
        return call()
    }
    return call(params.channel, params.allure)
}

def call(String channelParameter = null, allureParameter = null){
    def result = currentBuild.currentResult
    if (!colorMap.containsKey(result)){
        echo "slackSend is muted. Undefined build status: ${result}"
        return
    }
    def channel = channelParameter?.toString()?.trim()
    def allure = allureParameter?.toString()?.toBoolean()

    def slackParams = [
        color: colorMap.get(result),
        message: generateSlackMessage(allure)
    ]
    if (channel){
        slackParams.channel = "$channel"
    }
    slackSend slackParams

}

@Field
Map resultMessageMap = ['SUCCESS': 'build passed', 'FAILURE': 'build failed', 'UNSTABLE': 'tests failed']

private generateSlackMessage(Boolean allureIsUsed){
    def result = currentBuild.currentResult
    def resultMessage = resultMessageMap.get(result)
    def imageNameLocal = imageName()
    def message = "${imageNameLocal} branch ${BRANCH_NAME} ${resultMessage}!"
    if (allureIsUsed) {
        message += " <${BUILD_URL}allure/|build-tests>"
    }
    message += getAllureReportsMessage()
    def blueOceanPipelineUrl = "${JENKINS_URL}blue/organizations/jenkins/${imageNameLocal}/detail/${BRANCH_NAME}/${BUILD_ID}/pipeline/"
    message += " (<${blueOceanPipelineUrl}|${BUILD_ID}>)"
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
