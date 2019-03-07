def call(String namespace, Map helmArgs=[:], List<Map> postDeploy=[]) {
    call namespace: namespace, helmArgs: helmArgs, postDeploy: postDeploy
}

def call(Map params) {
    try {
        lock(resource: "${(String)params['namespace']}-${imageName()}", inversePrecedence: true) {
            helmLint namespace: (String)params['namespace'], set: (Map)params['helmArgs']
            helmUpgrade namespace: (String)params['namespace'], set: (Map)params['helmArgs']
            List<Map> jobList = []
            if (params.containsKey('postDeploy')) {
                jobList = (List<Map>)params['postDeploy']
            }
            if (jobList.size() < 1) {
                return
            }
            if (jobList.size() == 1) {
                sanitizeThenRun(jobList[0])
                return
            }
            def batch = [:]
            jobList.each { job ->
                batch.put(job['job']?.trim(), {
                    sanitizeThenRun(job)
                })
            }
            parallel(batch)
        }
    } catch (Exception e) {
        throw e
    } finally {
        milestone()
    }
}

def sanitizeThenRun(Map runnable) {
    String jobName = ""
    List parameters = []
    if (runnable.containsKey('job')) {
        jobName = runnable['job']
    } else {
        currentBuild.result = 'FAILURE'
        error "job name missing"
        return
    }
    if (runnable.containsKey('parameters')) {
        parameters = (List)runnable['parameters']
    }
    runTests job: jobName, parameters: parameters
}