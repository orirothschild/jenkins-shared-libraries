import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.Field

@Field
Map buildStateMap = ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED']

@Field
List bitbucketStatuses = ['INPROGRESS', 'SUCCESSFUL', 'FAILED']

def call(String statusParam=null, String repoSlugParam=null) {
    call status: statusParam, repoSlug: repoSlugParam
}

def call(Map params){

    def status=null
    def repoSlug=null

    if (params != null){
        status = params.status
        repoSlug = params.repoSlug
    }

    def bitbucketStatusNotifyParams = [:]
    String computedBitbucketStatus
    if (status == null) {
        computedBitbucketStatus = currentBuild.currentResult
        if (!buildStateMap.containsKey(computedBitbucketStatus)) {
            echo "bitbucketStatusNotify is muted. Undefined build status: ${computedBitbucketStatus}"
            return
        }
        bitbucketStatusNotifyParams.buildState = "${buildStateMap.get(computedBitbucketStatus)}"
    } else {
        computedBitbucketStatus = status.trim().toUpperCase()
        if (bitbucketStatuses.contains(computedBitbucketStatus)) {
            bitbucketStatusNotifyParams.buildState = "${computedBitbucketStatus}"
        } else {
            error "Undefined bitbucket status: ${status}"
        }
    }

    if (env.BRANCH_NAME == 'master'){
        echo "Bitbucket status '${computedBitbucketStatus}' is ignored cause 'master' branch"
        return
    }

    bitbucketStatusNotifyParams.commitId = "${commitId()}"
    def repoSlugLocal = repoSlug?.trim()
    if (repoSlugLocal == null || repoSlugLocal == ''){
        repoSlugLocal = "${imageName()}"
    }

    bitbucketStatusNotifyParams.repoSlug = repoSlugLocal

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
    def accessToken = getAuthorizationHeader()
    sendStatus(params, accessToken)
}

private Map getAuthorizationHeader(){
    def req = httpRequest   url: 'https://bitbucket.org/site/oauth2/access_token',
                            authentication: 'bitbucket-oauth-credentials',
                            httpMode: 'POST',
                            requestBody: 'grant_type=client_credentials',
                            contentType: 'APPLICATION_FORM',
                            validResponseCodes: '200:201',
                            consoleLogResponseBody: false

    def jsonSlurper = new JsonSlurper()
    def body = jsonSlurper.parseText(req.content)
    [ name: 'Authorization', value: "Bearer ${body['access_token']}".toString()]
}

private sendStatus(Map params, Map authorization){
    def bitbucketApiUrl = 'https://api.bitbucket.org/2.0/repositories/bilderlings'
    def url = "${bitbucketApiUrl}/${params.repoSlug}/commit/${params.commitId}/statuses/build"
    def blueOceanPipelineUrl = "${JENKINS_URL}blue/organizations/jenkins/${params.repoSlug}/detail/${BRANCH_NAME}/${BUILD_ID}/pipeline/"
    def data = [
            state: params.buildState,
            url: blueOceanPipelineUrl,
            key: params.repoSlug,
            name: "${JOB_NAME} #${BUILD_ID}"
    ]
    def body = JsonOutput.toJson(data)
    httpRequest url: url,
            authentication: 'bitbucket-oauth-credentials',
            httpMode: 'POST',
            requestBody: body,
            contentType: 'APPLICATION_JSON',
            customHeaders: [authorization],
            validResponseCodes: '200:201',
            consoleLogResponseBody: false
}
