
def call(){
    call(null)
}

def call(Map params){
    if (params == null){
        params = [:]
    }
    params.status = 'INPROGRESS'
    bitbucketStatus(params)
}
