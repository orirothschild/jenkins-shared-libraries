# Shared libraries
## Setup Jenkins
1. Open Jenkins -- Manage Jenkins -- Configure System
2. Find section "Global Pipeline Libraries"
3. Set following fields:
	* Name: shared_library (it will be used in Jenkinsfile with anotation @Library)
	* Default version: master or another branch, tag name from repository
	* Load implicitly: false (optional). If checked, scripts will automatically have access to this library without needing to request it via @Library
	* Allow default version to be overriden: true (optional)
	* Include @Library changes in job recent changes: true (optional)
	* Select "Modern SCM": Git with parameters:
		* Project Repository: https://github.com/bilderlings/jenkins-shared-libraries.git
		* Credentials: <empty>

## Add shared libraries to Jenkinsfile
For using Shared libraries You should add anotation at the beginning of Jenkinsfile: 

@Library(jenkins-shared-libraries@master) _

Here is:

* shared_library: name of shared libraries, which was set in "Setup Jenkins"
* After @ you might set branch or tag name. You can omit @branch_name, because Jenkins get default version from "Setup Jenkins"
* Underscore loads global variables to script in light mode without using "import" statement.

*Full description of Shared Libraries:* [here](https://jenkins.io/doc/book/pipeline/shared-libraries/)  

## Global variables


Variables for declarative pipeline:
    
* `bitbucketStatus([status: status_name, reposlug: repo_name, ignoreMaster: true(default), false])`
    * Valid status_name: INPROGRESS, SUCCESSFUL, FAILED. 
    * Other statuses will raise **error**
    * If **status_name** is not specified, **status_name** will be related to build status in jenkins.  
    * reposlug: override repository name. Default - imageName()
```groovy
post {
    always {
        bitbucketStatus()
    }
}
```
```groovy
steps {
    bitbucketStatus status: 'INPROGRESS', repoSlug: 'repoName', ignoreMaster: false
}
```
* `bitbucketStatusSuccessful()`
* `bitbucketStatusSuccessful([repoSlug: 'repo_name', ignoreMaster: true(default), false])`
* `bitbucketStatusFailed()`
* `bitbucketStatusFailed([repoSlug: 'repo_name', ignoreMaster: true(default), false])`
* `bitbucketStatusInProgress()`
* `bitbucketStatusInProgress([repoSlug: 'repo_name', ignoreMaster: true(default), false])`
* `commitId()`
    * Get last commit ID  
* `deploy([namespace: String, helmArgs: Map, postDeploy: List<Map>, valuesPath: String])`
    * **namespace** - k8s namespace of the deployment
    * **valuesPath** - custom path for values
    * **helmArgs** - --set arguments for helm
    * **postDeploy** - jobs to be run after the deployment (suitable for runTests)
    * Requires **helmUpgrade()**
    * Requires **runTests()**
    * Requires **imageName()**
    * Requires **Lockable Resources plugin**
```groovy
container('helm') {
    deploy namespace: 'test', valuesPath: 'values_path.yaml',
        helmArgs: ['image.tag': imageTag()],
        postDeploy: [
            [
                job: 'tests-api/master',
                parameters: [
                    string(name: 'TAGS', value: 'Project-1'),
                    booleanParam(name: 'RUN_TESTS', value: true)
                ]
            ],
            [
                job: 'tests-web/master',
                parameters: [string(name: 'TAGS', value: 'Project-1')]
            ]
        ]
}
```
* `dockerBuild([dockerfile: docker_file_path, imageName: "imagename"])`
    * Required **Multibranch plugin**
    * Required variable **env.DOCKER_REGISTRY**
    * Default **imageName**: global var imageName()
    * **imageName**: null, '', ' ' will be set default
    * Will be built with 2 tags: imageName:imageTag(), imageName:${BRANCH_NAME}-${BUILD_ID}
    * Default **docker_file_path**: './Dockerfile'
    * **docker_file_path**: null, '', ' ' will be set to default
    * Default **imageName**: first part of JOB_NAME
```groovy
steps {
    container('docker') {
        dockerBuild()
    }
}
```
or
```groovy
steps {
    container('docker') {
        dockerBuild dockerfile:'./files/DockerFile'
        dockerbuild imageName: 'any_name'
    }
}
```
* `dockerPush([imageName: 'imagename')`
    * Required **Multibranch plugin**
    * Required variable **env.DOCKER_REGISTRY**
    * Default **imageName**: global var imageName()
    * **imageName**: null, '', ' ' will be set to default
    * Will be pushed with with 2 tags: imageName:imageTag(), imageName:${BRANCH_NAME}-${BUILD_ID}
```groovy
steps {
    container('docker') {
        dockerPush()
    }
}
```  
```groovy
steps {
    container('docker') {
        dockerPush imegName: 'any_name'
    }
}
```   
* `dockerPushLatest([imageName: 'imagename'])`
    * *Tag and push* image on latest
    * Required **Multibranch plugin**
    * Required variable **env.DOCKER_REGISTRY**
    * Default **imageName**: first part of JOB_NAME
    * **imageName**: null, '', ' ' will be set to default
```groovy
steps {
    container('docker') {
        dockerPushLatest()
    }
}
```  
```groovy
steps {
    container('docker') {
        dockerPushLatest imagename: 'any_name'
    }
}
```    
* `helmLint([namespace: 'namespace', set: Map, valuesPath: String])`
    * **namespace** must be valid, not null, empty or only whitespaces
    * **helmArgs** will be set via --set
    * **valuesPath** - custom path for values
```groovy
steps {
    helmLint namespace: 'test', set: ['account.image.tag': imageTag()], valuesPath: 'values_path.yaml'
}
```  
* `helmUpgrade([namespace: 'namespace', set: Map, valuesPath: String])`
    * **namespace** must be valid, not null, empty or only whitespaces
    * **helmArgs** will be set via --set
    * **valuesPath** - custom path for values
```groovy
steps {
    helmUpgrade namespace: 'test', set: ['account.image.tag': imageTag()], valuesPath: 'values_path.yaml'
}
```   
* `kubernetesLabel()`
    * Replace "${BUILD_TAG.take(53)}-x"

* `runTests([job: jobName, parameters: Jenkins parameters])`
    * Started **job** step with parameters: [job: jobName, wait=true, propagate=false, parameters: Jenkins parameters]
    * If child build will return FAILURE, ABORTED, UNSTABLE - build will be set to UNSTABLE
    * You should check build status in next stages to avoid execution. UNSTABLE status will not stop next stages
```groovy
steps {    
    runTests job: 'JOB_NAME/master', parameters: [TAGS: 'TEST_CATEGORY']    
}
```   
* `slack([channel: channel_name, allure: boolean_value])`
	* Required **Multibranch plugin**
	* **Default channel** will be taken from Slack configuration in Jenkins
	* Build status will be taken from jenkins variable **currentBuild.currentResult**
	* Parameter **allure**: true(true, 'true', 'y', 1), false(false, 'false', null, '', ' ', 0, 'no')
	* Statuses are used: **SUCCESS, FAILURE, UNSTABLE**. Another ones will be ignored and you can see message in log.
```groovy
post {
    always {
        slack('#channel', 'y')
    }
}
```
or
```groovy
post {
    unstable {
        slack channel: '#channel1', allure: 'y'
    }
    failure {
        slack channel:'#channel2', allure: false
    }
}
```

* `imageName()`
    * Replace "${JOB_NAME}".split('/')[0]  
    
* `imageTag()`
    * Replace commitId().take(7)  
    
* `successBuild()`
    * Expression: currentBuild.currentResult == 'SUCCESS'