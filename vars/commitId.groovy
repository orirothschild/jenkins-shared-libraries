
def call(){
    def result = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'")
    if (env.PIPELINE_TESTS_MODE_ON?.toString() == 'YES'){
        return '80459e2f250a0b59b16762c8ac7b71417dc5e6ca'
    }
    result.trim()
}