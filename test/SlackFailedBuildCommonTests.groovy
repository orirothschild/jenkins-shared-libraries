import TestData.SlackTestData
import Utils.Helper

class SlackFailedBuildCommonTests extends GroovyTestCase {


    void test_SlackFailedBuild_ColorIsDanger(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedBuild()

        assertEquals('danger', actualParameters['color'])

    }

    void test_SlackFailedBuildDeclarative_ColorIsDanger(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        assertEquals('danger', actualParameters['color'])

    }

    void test_SlackFailedBuild_MessageIsCorrect(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedBuild()

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build failed! <FAKE_Build_Urlallure/|Allure report> (1234)'

        assertEquals(expectedMessage, actualParameters['message'])

    }

    void test_SlackFailedBuildDeclarative_MessageIsCorrect(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build failed! <FAKE_Build_Urlallure/|Allure report> (1234)'

        assertEquals(expectedMessage, actualParameters['message'])

    }

    void test_SlackFailedBuild_ChannelIsNotSpecified_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedBuild()

        assertNull(actualParameters['channel'])

    }

    void test_SlackFailedBuildDeclarative_ChannelIsNotSpecified_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        assertNull(actualParameters['channel'])

    }

    void test_SlackFailedBuild_ChannelIsSpecified_ChannelParameterIsTheSameAsInput(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedBuild("channelName")

        assertEquals('channelName', actualParameters['channel'])

    }

    void test_SlackFailedBuildDeclarative_ChannelIsSpecified_ChannelParameterIsTheSameAsInput(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_("channelName")

        assertEquals('channelName', actualParameters['channel'])

    }

}
