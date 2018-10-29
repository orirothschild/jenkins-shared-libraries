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
    argss << [args: ['account.image.tag': 'imageTag', 'swagger.image.tag': 'imageTag'],
              result: 'helm upgrade -f "chart/values-test.yaml" --install --force --wait --namespace "test" --set "account.image.tag=imageTag" --set "swagger.image.tag=imageTag" "FAKE_Job_Name-test" chart/']
    argss << [args: ['swagger.image.tag': 'imageTag'],
              result: 'helm upgrade -f "chart/values-test.yaml" --install --force --wait --namespace "test" --set "swagger.image.tag=imageTag" "FAKE_Job_Name-test" chart/']
    argss << [args: [:],
              result: 'helm upgrade -f "chart/values-test.yaml" --install --force --wait --namespace "test" "FAKE_Job_Name-test" chart/']
    argss << [args: null as Map,
              result: 'helm upgrade -f "chart/values-test.yaml" --install --force --wait --namespace "test" "FAKE_Job_Name-test" chart/']
    argss
}

static List correctNamespaces(){
    ['test', ' test ']
}

static List incorrectNamespaces(){
    [null, '', ' ']
}