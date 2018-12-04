
def call(){
    bitbucketStatus('SUCCESSFUL')
}

def call(Map params){
    if (params == null){
        call()
    } else {
        bitbucketStatus('SUCCESSFUL', params.repoSlug)
    }
}