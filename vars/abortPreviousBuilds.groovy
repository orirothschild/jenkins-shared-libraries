
def call() {
	def jobName = env.JOB_NAME
	def buildNumber = env.BUILD_NUMBER.toInteger()
	def currentJob = Jenkins.instance.getItemByFullName(jobName)

	for (def build : currentJob.builds) {
		if (build.isBuilding() && build.number.toInteger() < buildNumber) {
			build.doStop()
			echo "build ${build.number.toInteger()} is aborted"
		}
	}
}