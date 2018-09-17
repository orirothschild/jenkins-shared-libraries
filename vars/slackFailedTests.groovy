
def call(String slackChannel=null){

    def generatedMessage = generateTestsSlackMessage(true)
    if (slackChannel?.trim()) slackSend color: 'warning', message: generatedMessage, channel: slackChannel
    else slackSend color: 'warning', message: generatedMessage

}

private generateTestsSlackMessage(failed){

    def imageName = "${JOB_NAME}".split('/')[0]
    def failedString = failed? 'failed':'passed'
    "${imageName} branch ${BRANCH_NAME} tests ${failedString}! <${env.BUILD_URL}allure/|Allure report> (${env.BUILD_ID})"

}