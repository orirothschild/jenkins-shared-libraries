
def call(String slackChannel=null){

    def generatedMessage = generateBuildSlackMessage(true)
    if (slackChannel?.trim()) slackSend color: 'good', message: generatedMessage, channel: slackChannel
    else slackSend color: 'good', message: generatedMessage

}

private generateBuildSlackMessage(failed){

    def imageName = "${JOB_NAME}".split('/')[0]
    def failedString = failed? 'failed':'passed'
    "${imageName} branch ${BRANCH_NAME} build ${failedString}! <${env.BUILD_URL}allure/|Allure report> (${env.BUILD_ID})"

}
