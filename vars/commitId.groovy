
def call(){
    def gitCommit = env.GIT_COMMIT?.toString()?.trim()
    if (gitCommit != null && gitCommit != ''){
        echo 'Get git commit id from environment variable GIT_COMMIT'
        return gitCommit
    }

    sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'")

}