def call(String namespaceName, Map helmArgs=[:], Map runAfter=[:]) {
    call namespace: namespaceName, set: helmArgs, runAfter: runAfter
}

def call(Map params) {

}