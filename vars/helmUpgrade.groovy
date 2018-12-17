

def call(String namespaceParameter, Map args=[:]){
    call namespace: namespaceParameter, set: args
}

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
            exposedArgs += ' --set '
            exposedArgs += "\"$k=$v\""
        }
        exposedArgs += ' '
    }
    def releaseName = "${imageName()}-${namespace}"
    try{
        sh "helm upgrade -f \"chart/values-${namespace}.yaml\" --install --force --wait --namespace \"${namespace}\"${exposedArgs}\"${releaseName}\" chart/"
    }catch(Exception ex){
        if (ex.message.contains('UPGRADE FAILED')){
            // 0 - is previous revision; https://github.com/helm/helm/issues/1796#issuecomment-311385728
            sh "helm rollback --wait \"${releaseName}\" 0"
        } else {
            echo "It seems not a upgrading failure. If it's the failure, you can do 'helm rollback --wait \"${releaseName}\" 0'"
        }
        error "${ex}"
    }
}