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
    * Valid status_name: INPROGRESS, SUCCESSFUL, FAILED
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

* `dockerBuild(docker_file_path(String, optional))`
    * Required **Multibranch plugin**
    * Default **docker_file_path**: './Dockerfile'
    * Exception will thrown for incorrect values of dockerfile: null, '', ' '
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
        dockerBuild('./files/DockerFile')
    }
}
```
* `dockerPushLatest()`
    * Required **Multibranch plugin**
```groovy
steps {
    container('docker') {
        dockerPushLatest()
    }
}
```    
* `slack(channel_name(String, optional), allure(Boolean, optional, default: false))`    
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
        slack('#channel2', 'no')
    }
}
```