package Utils

import TestData.BuildResult

class Helper {

    static setEnvVariables(Map variables, Object... objs){
        objs.each { obj ->
            obj.env = variables
            variables.each { k, v ->
                obj."$k" = v
            }
        }
    }

    static addEnvVariables(Map variables, Object... objs){
        objs.each { obj ->
            obj.env << variables
            variables.each { k, v ->
                obj."$k" = v
            }
        }
    }

    static setBuildStatus(String result, Object obj){
        def buildResult = new BuildResult()
        buildResult['result'] = result
        obj['currentBuild'] = buildResult
    }

}
