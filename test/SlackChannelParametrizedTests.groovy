import TestData.SlackTestData
import Utils.Helper
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class SlackChannelParametrizedTests extends GroovyTestCase {

    @Parameterized.Parameters
    static Collection<String> data(){[ '', ' ', null ]}

    private String channelValue

    SlackChannelParametrizedTests(String channelValue){
        this.channelValue = channelValue
    }

    @Test
    void test_SlackFailedBuild_ChannelFalseValues_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedBuild(channelValue)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackFailedBuildDeclarative_ChannelFalseValues_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_(channelValue)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackSuccessBuild_ChannelFalseValues_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.successBuild(channelValue)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackSuccessBuildDeclarative_ChannelFalseValues_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackSuccessBuild()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_(channelValue)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackFailedTests_ChannelFalseValues_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slack()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_.failedTests(channelValue)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackFailedTestsDeclarative_ChannelFalseValues_ChannelParameterIsNull(){

        def slackVariables = SlackTestData.commonVariables()

        def slack_ = new slackFailedTests()
        Helper.setEnvVariable(slackVariables, slack_)

        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        slack_(channelValue)

        assertNull(actualParameters['channel'])

    }

}
