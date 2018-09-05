@Library('shared_library@master') _

node('master'){
    stage('Send slack correct notification'){

        slack.successBuild()
        slack.successBuild('')
        slack.successBuild(null)
        slack.successBuild('#jenkins_test')

        slack.failedBuild()
        slack.failedBuild('')
        slack.failedBuild(null)
        slack.failedBuild('#jenkins_test')

        slack.failedTests()
        slack.failedTests('')
        slack.failedTests(null)
        slack.failedTests('#jenkins_test')
    }

//    stage('Send slack incorrect notification'){
//
//        channelName = 10
//        slack.successBuild(channelName)
//
//    }

}