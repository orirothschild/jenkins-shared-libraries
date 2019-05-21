def call(String namespace, Map helmArgs=[:], List<Map> postDeploy=[]) {
    call namespace: namespace, helmArgs: helmArgs, postDeploy: postDeploy
}

def call(Map params) {
    def lockResourceName = params['lockResourceName']
	def inverseLockPrecedence = params['inverseLockPrecedence']

    if (lockResourceName == null || ((String)lockResourceName).trim() == ''){
        lockResourceName = "${(String)params['namespace']}-${imageName()}"
    }

	if (inverseLockPrecedence == null){
		inverseLockPrecedence = true
	}else {
		inverseLockPrecedence = inverseLockPrecedence.toString().toBoolean()
	}

    lock(resource: lockResourceName, inversePrecedence: inverseLockPrecedence) {
        milestone()
        helmLint namespace: (String)params['namespace'], set: (Map)params['helmArgs'], valuesPath: (String)params['helmValuesPath']
        helmUpgrade namespace: (String)params['namespace'], set: (Map)params['helmArgs'], valuesPath: (String)params['helmValuesPath']
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

}

def sanitizeThenRun(Map runnable) {
    String jobName = ""
    List parameters = []
    if (runnable.containsKey('job')) {
        jobName = runnable['job']
    } else {
        error "job name missing"
        return
    }
    if (runnable.containsKey('parameters')) {
        parameters = (List)runnable['parameters']
    }
    runTests job: jobName, parameters: parameters
}