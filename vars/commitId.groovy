
def call(){
    def gitCommit = env.GIT_COMMIT?.toString()?.trim()
    if (gitCommit != null && gitCommit != ''){
        return gitCommit
    }

    sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'")

}