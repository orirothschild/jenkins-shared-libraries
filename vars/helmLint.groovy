
def call(Map params){

    def namespace = null
    def valuesPath = null
    def args = [:]

    if (params != null){
        namespace = params.namespace?.trim()
        args = params.set
        valuesPath = params.valuesPath?.trim()
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

    def values = ' '
    if (valuesPath == null){
        values = """ -f "chart/values-${namespace}.yaml" """
    }else if(valuesPath != ''){
        values = """ -f "${valuesPath}" """
    }

    def helmLintLog = sh(returnStdout: true, script: '#!/bin/sh -e\n' + 'mktemp /tmp/helm_lint_log.XXXXXX').trim()
    try{
        def status = sh(returnStatus: true, script: "helm lint${values}--namespace \"${namespace}\"${exposedArgs}chart/ &>${helmLintLog}")
        def errorText = sh(returnStdout: true, script: '#!/bin/sh -e\n' + "cat ${helmLintLog}").trim()
        if (errorText) echo "${errorText}"
        sh(returnStatus: true, script: "helm template${values}--namespace \"${namespace}\"${exposedArgs}chart/")
        if (status != 0){
            if (errorText){
                errorText.split('\n').each {
                    if (it =~ /\[ERROR]/) {
                        //helm 2 always add error about chart name and directory
                        // it fixed in helm 3 - https://github.com/helm/helm/issues/1979#issuecomment-459770487
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
        sh  returnStdout: true, script: '#!/bin/sh -e\n' + "rm ${helmLintLog}"
    }
}