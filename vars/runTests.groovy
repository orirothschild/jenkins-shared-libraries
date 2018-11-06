
def call(Map params){
    def job = params.job
    def buildResult = build job: job,
                            propagate: false,
                            wait: true,
                            parameters: createStringParameters(params.parameters as Map)

    def normalizedJobName = job?.toString()?.split('/')[0]

    env["${normalizedJobName}_TESTS_URL"] = "${buildResult.absoluteUrl}allure/"
    if (buildResult.result != 'SUCCESS') currentBuild.result = 'FAILURE'

}

private createStringParameters(Map parameters){
    def stringParams = []
    parameters.each { k, v ->
        stringParams << string(name: k, value: v)
    }
    stringParams
}
