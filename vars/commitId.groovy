
def call(){
    def gitCommit = env.GIT_COMMIT?.ToString()?.trim()
    if (gitCommit != null && gitCommit != ''){
        return gitCommit
    }
    try{
        return sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'")
    } catch(Exception ex) {
        if (ex.message.contains('git: not found')){
            return sh(returnStdout: true, script: "cat ${env.WORKSPACE}/.git/HEAD | awk '{print \$2}' | xargs -I {} cat ${env.WORKSPACE}/.git/{}")
        }
        throw ex
    }
}