
def call(Map params){
    call(params.namespace as String, params.set as Map)
}

def call(String namespace, Map args=null){
    def namespaceLocal = namespace?.trim()
    if (!namespaceLocal){
        error "Undefined namespace: ${namespace}"
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
    def releaseName = "${imageName()}-${namespaceLocal}"
    try{
        sh "helm upgrade -f \"chart/values-${namespaceLocal}.yaml\" --install --force --wait --namespace \"${namespaceLocal}\"${exposedArgs}\"${releaseName}\" chart/"
    }catch(Exception ex){
        // 0 - is previous revision; https://github.com/helm/helm/issues/1796#issuecomment-311385728
        sh "helm rollback \"${releaseName}\" 0"
        error "${ex}"
    }
}
