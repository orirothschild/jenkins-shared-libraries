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

static resultMessageMap(){ ['SUCCESS': 'build passed', 'FAILURE': 'build failed', 'UNSTABLE': 'tests failed'] }

private generateSlackMessage(Boolean allureIsUsed){
    def result = currentBuild.currentResult
    def imageName = new imageName()
    imageName.binding = this.binding
    def imageNameLocal = "${imageName()}"
    def resultMessage = resultMessageMap()."${result}"
    def message
    message = "${imageNameLocal} branch ${BRANCH_NAME} ${resultMessage}!"
    if (allureIsUsed) {
        message += " <${BUILD_URL}allure/|Allure report>"
    }
    message += " (<${BUILD_URL}|${BUILD_ID}>)"
    message
}

