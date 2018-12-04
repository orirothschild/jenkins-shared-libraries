
def call(){
    bitbucketStatus('INPROGRESS')
}

def call(Map params){
    if (params == null){
        call()
    } else {
        bitbucketStatus('INPROGRESS', params.repoSlug)
    }
}
