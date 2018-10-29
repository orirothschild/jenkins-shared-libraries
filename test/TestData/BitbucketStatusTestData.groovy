package TestData

static Map commonVariables(){
    [
        JOB_NAME: "Job_Name/master",
        BUILD_ID: 2,
        JENKINS_URL: 'http://jenkins.k8s.iamoffice.lv/',
        BRANCH_NAME: 'master'
    ]
}
static Map commonVariablesForFirstBuild(){
    [
        JOB_NAME: "Job_Name/master",
        BUILD_ID: 1,
        JENKINS_URL: 'http://jenkins.k8s.iamoffice.lv/',
        BRANCH_NAME: 'master'
    ]
}

static httpRequestMock(Map map){
    if (map.url.toString()?.contains('/statuses/build') && map.httpMode.toString()?.equals('POST')){
        return  [status: '200', content: '']
    }
    if ("${map.url}" == 'https://bitbucket.org/site/oauth2/access_token' &&
        "${map.httpMode}" == 'POST' &&
        "${map.requestBody}".contains('grant_type=client_credentials')){
        def content = '{"access_token": "fake_access_token=", "scopes": "repository:write", "expires_in": 7200, "refresh_token": "fake_refresh_token=", "token_type": "bearer"}'
        return [status: '200', content: content]
    }

}

static buildStateMap(){ ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED'] }

static List<Object[]> suite_DefinedBuildStatus_ValidBitbucketStatuses(){
    TestDataCombination.join(definedBuildStatuses(), validBitbucketStatuses())
}

static List<Object[]> suite_UndefinedBuildStatus_ValidBitbucketStatuses(){
    TestDataCombination.join(undefinedBuildStatuses(), validBitbucketStatuses())
}

static List<Object[]> suite_DefinedBuildStatus_InvalidBitbucketStatuses(){
    TestDataCombination.join(definedBuildStatuses(), invalidBitbucketStatuses())
}

static List<Object[]> suite_UndefinedBuildStatus_InvalidBitbucketStatuses(){
    TestDataCombination.join(undefinedBuildStatuses(), invalidBitbucketStatuses())
}

static definedBuildStatuses(){
    ['SUCCESS', 'UNSTABLE', 'FAILURE']
}

static undefinedBuildStatuses(){
    ['UNDEFINED', '', ' ', null, 'ABORTED']
}

static validBitbucketStatuses(){
    ['INPROGRESS', 'SUCCESSFUL', 'FAILED']
}

static invalidBitbucketStatuses(){
    ['', ' ', 'custom']
}