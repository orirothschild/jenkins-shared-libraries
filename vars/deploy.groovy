def call(String namespace, Map helmArgs=[:], List<Map> postDeploy=[]) {
    call namespace: namespace, helmArgs: helmArgs, postDeploy: postDeploy
}

def call(Map params) {
    lock(resource: "deployment-${imageName()}", inversePrecedence: true) {
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
        for (job in jobList) {
            batch.put(job['job']?.trim(), {
                sanitizeThenRun(job)
            })
        }
        parallel(batch)
    }
}

def sanitizeThenRun(Map runnable) {
    String jobName = ""
    List parameters = []
    if (runnable.containsKey('job')) {
        jobName = runnable['job']
    } else {
        error "job name missing"
    }
    if (runnable.containsKey('parameters')) {
        parameters = (List)runnable['parameters']
    }
    runTests job: jobName, parameters: parameters
}