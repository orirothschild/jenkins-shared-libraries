import groovy.transform.Field

@Field
Map buildStateMap = ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED']

@Field
List bitbucketStatuses = ['INPROGRESS', 'SUCCESSFUL', 'FAILED']

def call(String status=null){
    def bitbucketStatusNotifyParams = [:]
    if (status == null){
        def result = currentBuild.currentResult
        if (!buildStateMap.containsKey(result)){
            echo "bitbucketStatusNotify is muted. Undefined build status: ${result}"
            return
        }
        bitbucketStatusNotifyParams.buildState = "${buildStateMap.get(result)}"
    }else {
        def statusUpperCase = status.trim().toUpperCase()
        if (bitbucketStatuses.contains(statusUpperCase)) {
            bitbucketStatusNotifyParams.buildState = "${statusUpperCase}"
        }
        else {
            error "Undefined bitbucket status: ${status}"
        }
    }

    bitbucketStatusNotifyParams.commitId ="${commitId()}"
    bitbucketStatusNotifyParams.repoSlug = "${imageName()}"

    bitbucketStatusNotify bitbucketStatusNotifyParams

}