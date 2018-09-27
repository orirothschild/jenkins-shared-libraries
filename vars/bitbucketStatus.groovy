
static buildStateMap(){ ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED'] }

def call(){
    def result = currentBuild.currentResult
    if (!buildStateMap().containsKey(result)){
        echo "bitbucketStatusNotify is muted. Undefined build status: ${result}"
        return
    }

    def imageName = "${JOB_NAME}".split('/')[0]
    def commitId = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'").trim()
    def bitbucketStatusNotifyParams = [
        buildState: buildStateMap().get(result),
        commitId: "${commitId}",
        repoSlug: "${imageName}"
    ]

    bitbucketStatusNotify bitbucketStatusNotifyParams

}
