
def call(Map params){

    def namespace = null
    def args = [:]

    if (params != null){
        namespace = params.namespace?.trim()
        args = params.set
    }

    if (!namespace){
        currentBuild.result = 'FAILURE'
        error "Undefined namespace: ${params.namespace}"
    }
    def exposedArgs = ' '
    if (args && args.size() > 0){
        exposedArgs = ''
        args.each { k, v ->
            if (v instanceof CharSequence){
                exposedArgs += ' --set-string '
            } else {
                exposedArgs += ' --set '
            }
            exposedArgs += "\"$k=$v\""
        }
        exposedArgs += ' '
    }
    def helmLintLog = sh(returnStdout: true, script: 'mktemp /tmp/helm_lint_log.XXXXXX').trim()
    try{
        def status = sh(returnStatus: true, script: "helm lint -f \"chart/values-${namespace}.yaml\" --namespace \"${namespace}\"${exposedArgs}chart/ &>${helmLintLog}")
        def errorText = sh(returnStdout: true, script: "cat ${helmLintLog}").trim()
        if (errorText) echo "${errorText}"
        sh(returnStatus: true, script: "helm template -f \"chart/values-${namespace}.yaml\" --namespace \"${namespace}\"${exposedArgs}chart/")
        if (status != 0){
            if (errorText){
                errorText.split('\n').each {
                    if (it =~ /\[ERROR]/) {
                        //helm 2 always add error about chart name and directory
                        // it fixed in helm 3 - https://github.com/helm/helm/issues/1979#issuecomment-459770487
                        def findWarning = it =~ /Chart\.yaml: directory name \(.*\) and chart name \(.*\) must be the same/
                        if (!findWarning) {
                            currentBuild.result = 'FAILURE'
                            error "Helm lint exit code ${status}\n${errorText}"
                        }
                    }
                }
            } else {
                currentBuild.result = 'FAILURE'
                error "Helm lint exit code ${status}"
            }
        }
    }finally{
        sh  returnStdout: true, script: "rm ${helmLintLog}"
    }
}