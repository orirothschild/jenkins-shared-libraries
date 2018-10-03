import groovy.transform.Field

@Field
Map buildStateMap = ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED']

def call(String status=null){
    def bitbucketStatusNotifyParams = [
            commitId: "${commitId()}",
            repoSlug: "${imageName()}"
    ]
    if (!status?.trim()){
        def result = currentBuild.currentResult
        if (!buildStateMap.containsKey(result)){
            echo "bitbucketStatusNotify is muted. Undefined build status: ${result}"
            return
        }
        bitbucketStatusNotifyParams.buildState = "${buildStateMap.get(result)}"
    }else {
        bitbucketStatusNotifyParams.buildState = "${status}"
    }

    bitbucketStatusNotify bitbucketStatusNotifyParams

}