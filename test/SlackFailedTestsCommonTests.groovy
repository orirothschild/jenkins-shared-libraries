import TestData.SlackTestData
import Utils.Helper

class SlackFailedTestsCommonTests extends GroovyTestCase {


    void test_SlackFailedTests_ColorIsWarning(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedTests()

        assertEquals('warning', actualParameters['color'])

    }

    void test_SlackFailedTestsDeclarative_ColorIsWarning(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedTests()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        assertEquals('warning', actualParameters['color'])

    }

    void test_SlackFailedTests_MessageIsCorrect(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedTests()

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name tests failed! <FAKE_Build_Urlallure/|Allure report> (1234)'

        assertEquals(expectedMessage, actualParameters['message'])

    }

    void test_SlackFailedTestsDeclarative_MessageIsCorrect(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedTests()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name tests failed! <FAKE_Build_Urlallure/|Allure report> (1234)'

        assertEquals(expectedMessage, actualParameters['message'])

    }

    void test_SlackFailedTests_ChannelIsNotSpecified_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedTests()

        assertNull(actualParameters['channel'])

    }

    void test_SlackFailedTestsDeclarative_ChannelIsNotSpecified_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedTests()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        assertNull(actualParameters['channel'])

    }

    void test_SlackFailedTests_ChannelIsSpecified_ChannelParameterIsTheSameAsInput(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedTests("channelName")

        assertEquals('channelName', actualParameters['channel'])

    }

    void test_SlackFailedTestsDeclarative_ChannelIsSpecified_ChannelParameterIsTheSameAsInput(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedTests()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_("channelName")

        assertEquals('channelName', actualParameters['channel'])

    }

}
