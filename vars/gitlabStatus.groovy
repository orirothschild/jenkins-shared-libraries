import groovy.transform.Field
import groovy.json.JsonOutput

@Field
Map jenkinsStatusToGitlabState = [
        'ABORTED'  : 'canceled',
        'FAILURE'  : 'failed',
        'NOT_BUILT': 'canceled',
        'SUCCESS'  : 'success',
        'UNSTABLE' : 'failed'
]

def call() {
    call(null)
}

def call(Map params) {
    withCredentials([string(credentialsId: 'gitlab_api_token', variable: 'TOKEN')]) {
        // Parameters
        def isRunning = false

        if (params != null) {
            isRunning = params.isRunning ?: false
        }

        // Data
        String remote = sh(returnStdout: true, script: "git config remote.origin.url")
        def project = remote.replaceFirst(".*gitlab.com(:|/)(.*)/(.*)/(.*).git", '$2%2F$3%2F$4').trim()
        def commitId = commitId()

        def state = isRunning ? 'running' : jenkinsStatusToGitlabState.get(currentBuild.currentResult)
        def branch = env.BRANCH_NAME
        def name = "Jenkins #${env.BUILD_NUMBER}"
        def targetUrl = env.BUILD_URL

        def data = [
                state     : state,
                ref       : branch,
                name      : name,
                target_url: targetUrl
        ]

        println "Gitlab update status for ${currentBuild.currentResult} with $data"

        // Update status
        httpRequest url: "https://gitlab.com/api/v4/projects/$project/statuses/$commitId",
                httpMode: 'POST',
                requestBody: JsonOutput.toJson(data),
                contentType: 'APPLICATION_JSON',
                customHeaders: [
                        [name: 'private-token', value: TOKEN]
                ],
                validResponseCodes: '200:201',
                consoleLogResponseBody: false

        println "Gitlab build status updated to $state for $commitId"
    }
}
