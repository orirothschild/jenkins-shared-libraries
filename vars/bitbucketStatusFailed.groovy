
def call(){
    call(null)
}

def call(Map params){
    if (params == null){
        params = [:]
    }
    params.status = 'FAILED'
    bitbucketStatus(params)
}