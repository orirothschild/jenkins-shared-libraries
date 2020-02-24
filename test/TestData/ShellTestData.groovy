package TestData

static Closure getShellClosureSSH(){
    return { Map map ->
        if (map.returnStdout) {
            if (map.script == "git log -n 1 --pretty=format:'%H'"){
                return "1111111222222222222222222222222222222222"
            }
            if (map.script == "git config remote.origin.url"){
                return "git@gitlab.com:company/project/repository.git"
            }
        }
        throw new Exception("Undefined shell parameters: ${map}")
    }
}

static Closure getShellClosureHTTPS(){
    return { Map map ->
        if (map.returnStdout) {
            if (map.script == "git log -n 1 --pretty=format:'%H'"){
                return "1111111222222222222222222222222222222222"
            }
            if (map.script == "git config remote.origin.url"){
                return "https://gitlab.com/company/project/repository.git"
            }
        }
        throw new Exception("Undefined shell parameters: ${map}")
    }
}
