package TestData

static Map commonVariables(){ [
        JOB_NAME: "FAKE_Job_Name/master"
    ]
}

static List<Map> suite_correctNamespaceCorrectArgsCases(){
    def namespaces = correctNamespaces()
    def cases = []
    namespaces.each { namespace ->
        def argss = correctArgs()
        argss.each { argsCase ->
            argsCase.namespace = namespace
            cases << argsCase
        }
    }
    cases
}

static List<Map> suite_incorrectNamespaceCorrectArgsCases(){
    def namespaces = incorrectNamespaces()
    def cases = []
    namespaces.each { namespace ->
        def argss = correctArgs()
        argss.each { argsCase ->
            argsCase.namespace = namespace
            argsCase.remove('result')
            cases << argsCase
        }
    }
    cases
}

static List<Map> correctArgs(){
    def argss = []
    def tag = 1234
    argss << [args: ['account.image.tag': 'imageTag', 'swagger.image.tag': 1234, 'any.image.tag': "${tag}"],
              result: [
                      'helm lint -f "chart/values-test.yaml" --namespace "test" --set-string "account.image.tag=imageTag" --set "swagger.image.tag=1234" --set-string "any.image.tag=1234" chart/',
                      'helm template -f "chart/values-test.yaml" --namespace "test" --set-string "account.image.tag=imageTag" --set "swagger.image.tag=1234" --set-string "any.image.tag=1234" chart/'
              ]
    ]
    argss << [args: ['swagger.image.tag': 'imageTag'],
              result: [
                      'helm lint -f "chart/values-test.yaml" --namespace "test" --set-string "swagger.image.tag=imageTag" chart/',
                      'helm template -f "chart/values-test.yaml" --namespace "test" --set-string "swagger.image.tag=imageTag" chart/'
              ]
    ]
    argss << [args: [:],
              result: [
                      'helm lint -f "chart/values-test.yaml" --namespace "test" chart/',
                      'helm template -f "chart/values-test.yaml" --namespace "test" chart/'
              ]
    ]
    argss << [args: null as Map,
              result: [
                      'helm lint -f "chart/values-test.yaml" --namespace "test" chart/',
                      'helm template -f "chart/values-test.yaml" --namespace "test" chart/'
              ]
    ]
    argss
}

static List correctNamespaces(){
    ['test', ' test ']
}

static List incorrectNamespaces(){
    [null, '', ' ']
}