
def call(){
    bitbucketStatus('FAILED')
}

def call(Map params){
    if (params == null){
        call()
    } else {
        bitbucketStatus('FAILED', params.repoSlug)
    }
}