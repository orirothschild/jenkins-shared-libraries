
def call(Map params){
    def job = params.job
    def buildResult = build job: job,
                            propagate: false,
                            wait: true,
                            parameters: params.parameters

    def normalizedJobName = job?.toString()?.split('/')[0]

    env["${normalizedJobName}_TESTS_URL"] = "${buildResult.absoluteUrl}allure/"
    if (!successBuild(buildResult)){
        echo "ERROR: ${buildResult.fullDisplayName} completed with status ${buildResult.currentResult}"
        currentBuild.result = 'UNSTABLE'
    }

}
