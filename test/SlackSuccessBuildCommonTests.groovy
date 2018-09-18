import TestData.SlackTestData
import Utils.Helper

class SlackSuccessBuildCommonTests extends GroovyTestCase {


    void test_SlackSuccessBuild_ColorIsGood(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.successBuild()

        assertEquals('good', actualParameters['color'])

    }

    void test_SlackSuccessBuildDeclarative_ColorIsGood(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackSuccessBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        assertEquals('good', actualParameters['color'])

    }

    void test_SlackSuccessBuild_MessageIsCorrect(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.successBuild()

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <FAKE_Build_Urlallure/|Allure report> (1234)'

        assertEquals(expectedMessage, actualParameters['message'])

    }

    void test_SlackSuccessBuildDeclarative_MessageIsCorrect(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackSuccessBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <FAKE_Build_Urlallure/|Allure report> (1234)'

        assertEquals(expectedMessage, actualParameters['message'])

    }

    void test_SlackSuccessBuild_ChannelIsNotSpecified_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.successBuild()

        assertNull(actualParameters['channel'])

    }

    void test_SlackSuccessBuildDeclarative_ChannelIsNotSpecified_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackSuccessBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_()

        assertNull(actualParameters['channel'])

    }

    void test_SlackSuccessBuild_ChannelIsSpecified_ChannelParameterIsTheSameAsInput(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.successBuild("channelName")

        assertEquals('channelName', actualParameters['channel'])

    }

    void test_SlackSuccessBuildDeclarative_ChannelIsSpecified_ChannelParameterIsTheSameAsInput(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackSuccessBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_("channelName")

        assertEquals('channelName', actualParameters['channel'])

    }

}
