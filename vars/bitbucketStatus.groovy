import groovy.json.JsonOutput
import groovy.transform.Field

@Field
Map buildStateMap = ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED']

@Field
List bitbucketStatuses = ['INPROGRESS', 'SUCCESSFUL', 'FAILED']

def call(String status=null) {
    def bitbucketStatusNotifyParams = [:]
    if (status == null) {
        def result = currentBuild.currentResult
        if (!buildStateMap.containsKey(result)) {
            echo "bitbucketStatusNotify is muted. Undefined build status: ${result}"
            return
        }
        bitbucketStatusNotifyParams.buildState = "${buildStateMap.get(result)}"
    } else {
        def statusUpperCase = status.trim().toUpperCase()
        if (bitbucketStatuses.contains(statusUpperCase)) {
            bitbucketStatusNotifyParams.buildState = "${statusUpperCase}"
        } else {
            error "Undefined bitbucket status: ${status}"
        }
    }
    bitbucketStatusNotifyParams.commitId = "${commitId()}"
    bitbucketStatusNotifyParams.repoSlug = "${imageName()}"

    send(bitbucketStatusNotifyParams)
}


private send(Map params){

    if ("${BUILD_ID}" == '1'){
        try {
            sendViaAPI(params)
        }catch(MissingMethodException e){
            throw e
        }catch(Exception e){
            echo "${e}"
            bitbucketStatusNotify params
        }
    }else {
        bitbucketStatusNotify params
    }
}

private sendViaAPI(Map params){
    def bitbucketApiUrl = 'https://api.bitbucket.org/2.0/repositories/bilderlings'
    if (env.BITBUCKET_API_URL){
        bitbucketApiUrl = "${env.BITBUCKET_API_URL}"
    }
    def url = "${bitbucketApiUrl}/${params.repoSlug}/commit/${params.commitId}/statuses/build"
    def data = [
            state: params.buildState,
            url: url,
            key: params.repoSlug
    ]
    def body = JsonOutput.toJson(data)
    httpRequest url: url,
                authentication: 'bitbucket-oauth-credentials',
                httpMode: 'POST',
                requestBody: body,
                contentType: 'APPLICATION_JSON',
                validResponseCodes: '200:201',
                consoleLogResponseBody: true
}
