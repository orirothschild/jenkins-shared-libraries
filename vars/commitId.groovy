
def call(){
    sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'")
}