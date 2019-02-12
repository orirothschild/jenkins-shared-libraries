
def call(Map params){

    def namespace = null
    def args = [:]

    if (params != null){
        namespace = params.namespace?.trim()
        args = params.set
    }

    if (!namespace){
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
                errorText.eachLine {
                    if (it =~ /\[ERROR]/) {
                        def findWarning = it =~ /Chart\.yaml: directory name \(.*\) and chart name \(.*\) must be the same/
                        if (!findWarning) {
                            error "Helm lint exit code ${status}\n${errorText}"
                        }
                    }
                }
            } else {
                error "Helm lint exit code ${status}"
            }
        }
    }finally{
        sh  returnStdout: true, script: "rm ${helmLintLog}"
    }
}