# Shared libraries
Current was used Jenkins with version 2.121.3
##Setup Jenkins
1. Open Jenkins -- Manage Jenkins -- Configure System
2. Find section "Global Pipeline Libraries"
3. Set following fields:
	* Name: shared_library (it will be used in Jenkinsfile with anotation @Library)
	* Default version: master or another branch, tag name from repository
	* Load implicitly: false (optional)
	* Allow default version to be overriden: true (optional)
	* Include @Library changes in job recent changes: true (optional)
	* Select "Modern SCM": Git with parameters:
		* Project Repository: https://bitbucket.org/pbakulev/jenkins-shared-libraries.git
		* Credentials: < here is your credentials >

## Add shared libraries to Jenkinsfile
For using Shared libraries You should add anotation at the beginning of Jenkinsfile: 

@Library(shared_library@master) _

Here is:

* shared_library: name of shared libraries, which was set in "Setup Jenkins"
* After @ you might set branch or tag name. You can omit @branch_name, because Jenkins get default version from "Setup Jenkins"
* Underscore loads global variables to script in light mode without using "import" statement.

*Full description of Shared Libraries:* [here](https://jenkins.io/doc/book/pipeline/shared-libraries/)  

## Global variables
Use this as 'name_of_groovy_class'.'public_method_name'(parameters)

Variables:

* Send to slack 'successful build' notification:
	* slack.successBuild() - default channel from SlackJenkins configuration
	* slack.successBuild(name_of_channel)
* Send to slack 'failed build' notification:
	* slack.failedBuild() - default channel from SlackJenkins configuration
	* slack.failedBuild(name_of_channel)
* Send to slack 'failed tests' notification:
	* slack.failedTests() - default channel from SlackJenkins configuration
	* slack.failedTests(name_of_channel)