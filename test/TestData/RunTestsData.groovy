package TestData

static Map commonVariables(){
    [:]
}

static defaultSuccessBuildResult(){
    defaultBuildResult('SUCCESS')
}

static defaultBuildResult(String result = null){
    def buildResult = new BuildResult()
    buildResult['absoluteUrl'] = 'http://localhost:8080/job/child/1/'
    buildResult['fullDisplayName'] ='build #1'
    if (result != null) buildResult['result'] = result

    buildResult
}