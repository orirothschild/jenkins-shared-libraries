
def call(Map params){
    call(params.channel, params.allure)
}

static colorMap(){ ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'warning'] }

def call(channelParameter = null, allureParameter = null){
    def result = currentBuild.currentResult
    if (!colorMap().containsKey(result)){
        echo "slackSend is muted. Undefined build status: ${result}"
        return
    }
    def channel = channelParameter?.toString()?.trim()
    def allure = allureParameter?.toString()?.toBoolean()

    def slackParams = [
        color: colorMap().get(result),
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
    def imageName = "${JOB_NAME}".split('/')[0]
    def resultMessage = resultMessageMap()."${result}"
    def message
    message = "${imageName} branch ${BRANCH_NAME} ${resultMessage}!"
    if (allureIsUsed) {
        message += " <${BUILD_URL}allure/|Allure report>"
    }
    message += " (<${BUILD_URL}|${BUILD_ID}>)"
    message
}

