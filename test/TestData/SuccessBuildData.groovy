package TestData

static successBuildResult(){
    def buildResult = new BuildResult()
    buildResult['result'] = 'SUCCESS'
    buildResult
}

static failureBuildResult(){
    def buildResult = new BuildResult()
    buildResult['result'] = 'FAILURE'
    buildResult
}

static abortedBuildResult(){
    def buildResult = new BuildResult()
    buildResult['result'] = 'ABORTED'
    buildResult
}

static unstableBuildResult(){
    def buildResult = new BuildResult()
    buildResult['result'] = 'UNSTABLE'
    buildResult
}

static unknownBuildResult(){
    def buildResult = new BuildResult()
    buildResult['result'] = 'UNKNOWN'
    buildResult
}