
static buildStateMap(){ ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED'] }

def call(){
    def result = currentBuild.currentResult
    if (!buildStateMap().containsKey(result)){
        echo "bitbucketStatusNotify is muted. Undefined build status: ${result}"
        return
    }

    def bitbucketStatusNotifyParams = [
        buildState: buildStateMap().get(result),
        commitId: "${env.COMMIT_ID}",
        repoSlug: "${env.IMAGE_NAME}"
    ]

    bitbucketStatusNotify bitbucketStatusNotifyParams

}
