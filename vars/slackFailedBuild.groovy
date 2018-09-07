
def call(String slackChannel=null){

    generatedMessage = generateBuildSlackMessage(false)
    if (slackChannel?.trim()) slackSend color: 'good', message: generatedMessage, channel: slackChannel
    else slackSend color: 'good', message: generatedMessage

}

private generateBuildSlackMessage(failed){

    imageName = "${JOB_NAME}".split('/')[0]
    failedString = failed? 'failed':'passed'
    "${imageName} branch ${BRANCH_NAME} build ${failedString}! <${env.BUILD_URL}allure/|Allure report> (${env.BUILD_ID})"

}
