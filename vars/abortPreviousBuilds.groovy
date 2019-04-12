
def call() {
	def jobName = env.JOB_NAME
	def buildNumber = env.BUILD_NUMBER.toInteger()
	def currentJob = Jenkins.instance.getItemByFullName(jobName)

	def stoppedBuild = false
	for (def build : currentJob.builds) {
		if (build.isBuilding() && build.number.toInteger() < buildNumber) {
			build.doStop()
			echo "build ${build.number.toInteger()} is aborted"
			stoppedBuild = true
		}
	}
	if (!stoppedBuild){
		echo "No previous builds are in building status"
	}
}