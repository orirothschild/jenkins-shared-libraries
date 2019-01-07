package TestData

static Closure getLastCommitIdClosure(){
    return { Map map ->
        getLastCommitId(map)
    }
}

static getLastCommitId(Map map){
    if (map.returnStdout && map.script == "git log -n 1 --pretty=format:'%H'"){
        return "1111111222222222222222222222222222222222"
    }
    throw new Exception("Undefined shell parameters for getting last commit id: ${map}")
}