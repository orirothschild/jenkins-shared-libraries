package TestData

static Closure getShellClosure(){
    return { Map map ->
        if (map.returnStdout) {
            if (map.script == "git log -n 1 --pretty=format:'%H'"){
                return "1111111222222222222222222222222222222222"
            }
            if (map.script == "git config remote.origin.url"){
                return "git@github.com:bilderlings/jenkins-shared-libraries.git"
            }
        }
        throw new Exception("Undefined shell parameters: ${map}")
    }
}
