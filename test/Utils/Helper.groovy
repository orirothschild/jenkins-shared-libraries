package Utils

class Helper {

    static setEnvVariables(Map variables, Object... objs){
        objs.each { obj ->
            obj.env = variables
            variables.each { k, v ->
                obj."$k" = v
            }
        }
    }

    static setBuildStatus(String result, Object obj){
        obj['currentBuild'] = new Expando(currentResult: result)
    }

}
