package Utils

class Helper {

    static setEnvVariable(Map variables, Object obj){
        obj.env = variables
        variables.each { k, v ->
            obj."$k" = v
        }
    }

    static setBuildStatus(String result, Object obj){
        obj['currentBuild'] = new Expando(currentResult: result)
    }
}
