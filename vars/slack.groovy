
def successBuild(String slackChannel=null){

    def generatedMessage = generateBuildSlackMessage(false)
    if (slackChannel?.trim()) slackSend color: 'good', message: generatedMessage, channel: slackChannel
    else slackSend color: 'good', message: generatedMessage

}

def failedBuild(String slackChannel=null){

    def generatedMessage = generateBuildSlackMessage(true)
    if (slackChannel?.trim()) slackSend color: 'danger', message: generatedMessage, channel: slackChannel
    else slackSend color: 'danger', message: generatedMessage

}

private generateBuildSlackMessage(failed){

    def imageName = "${JOB_NAME}".split('/')[0]
    def failedString = failed? 'failed':'passed'
    "${imageName} branch ${BRANCH_NAME} build ${failedString}! <${env.BUILD_URL}allure/|Allure report> (${env.BUILD_ID})"

}

def failedTests(String slackChannel=null){

    def generatedMessage = generateTestsSlackMessage(true)
    if (slackChannel?.trim()) slackSend color: 'warning', message: generatedMessage, channel: slackChannel
    else slackSend color: 'warning', message: generatedMessage

}

private generateTestsSlackMessage(failed){

    def imageName = "${JOB_NAME}".split('/')[0]
    def failedString = failed? 'failed':'passed'
    "${imageName} branch ${BRANCH_NAME} tests ${failedString}! <${env.BUILD_URL}allure/|Allure report> (${env.BUILD_ID})"

}
