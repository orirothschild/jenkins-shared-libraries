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
    
* `bitbucketStatus(status_name(optional))`
    * Valid status_name: INPROGRESS, SUCCESSFUL, FAILED. 
    * Other statuses will raise **error**
    * If **status_name** is not specified, **status_name** will be related to build status in jenkins.  
```groovy
post {
    always {
        bitbucketStatus()
    }
}
```
or
```groovy
steps {
    bitbucketStatus("INPROGRESS")
}
```
* `bitbucketStatusSuccessful()`
* `bitbucketStatusFailed()`
* `bitbucketStatusInProgress()`
* `commitId()`
    * Get last commit ID  

* `dockerBuild(docker_file_path(String, optional), imageName(String, optional))`
* `dockerBuild([dockerfile: docker_file_path, imageName: imagename])`
    * Required **Multibranch plugin**
    * Required variable **env.DOCKER_REGISTRY**
    * Default **docker_file_path**: './Dockerfile'
    * **docker_file_path**: null, '', ' ' will be set to default
    * Default **imageName**: first part of JOB_NAME
    * **imageName**: null, '', ' ' will be set to default
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
        dockerBuild('./files/DockerFile', 'any_name')
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
* `dockerPush(imageName(String, optional))`
    * Required **Multibranch plugin**
    * Required variable **env.DOCKER_REGISTRY**
    * Default **imageName**: first part of JOB_NAME
    * **imageName**: null, '', ' ' will be set to default
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
        dockerPush('any_name')
    }
}
```   
* `dockerPushLatest(imageName(String, optional))`
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
        dockerPushLatest('any_name')
    }
}
```    
* `helmUpgrade(namespace(String), helmArgs(Map))`
    * **namespace** must be valid, not null, empty or only whitespaces
    * **helmArgs** will be set via --set
```groovy
steps {
    helmUpgrade('test', ['account.image.tag': imageTag()])
}
```   
* `kubernetesLabel()`
    * Replace "${BUILD_TAG.take(53)}-x"

* `slack(channel_name(String, optional), allure(Boolean, optional, default: false))`    
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
        slack('#channel1', 'y')
    }
    failure {
        slack channel:'#channel2', allure: false
    }
}
```

* `imageName()`
    * Replace "${JOB_NAME}".split('/')[0]  
    
* `imageTag()`
    * Replace "${BRANCH_NAME}-${BUILD_ID}"  
