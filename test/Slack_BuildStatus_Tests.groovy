import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class Slack_BuildStatus_Tests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        SlackTestData.suite_ChannelIsDefined_AllureIsAny() +
        SlackTestData.suite_ChannelIsEmpty_AllureIsCorrect() +
        SlackTestData.suite_ChannelIsWhitespace_AllureIsAny() +
        SlackTestData.suite_ChannelIsNull_AllureIsAny()
    }

    protected channel
    protected allure
    protected slack_ = new slack()

    Slack_BuildStatus_Tests(List list){
        this.channel = list[0]
        this.allure = list[1]
    }

    @Before
    void setUp(){
        def variables = SlackTestData.commonVariables()
        Helper.setEnvVariables(variables, slack_)
        InjectVars.injectTo(slack_, 'imageName')
    }

    @Test
    void test_Slack_BuildSuccessStatus_slackSendIsExecuted(){
        Helper.setBuildStatus('SUCCESS', slack_)
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}

        slack_(channel, allure)

        assertTrue(slackSendWasExecuted)

    }

    @Test
    void test_Slack_BuildFailureStatus_slackSendIsExecuted(){
        Helper.setBuildStatus('FAILURE', slack_)
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}

        slack_(channel, allure)

        assertTrue(slackSendWasExecuted)

    }

    @Test
    void test_Slack_BuildUnstableStatus_slackSendIsExecuted(){
        Helper.setBuildStatus('UNSTABLE', slack_)
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}

        slack_(channel, allure)

        assertTrue(slackSendWasExecuted)

    }

    @Test
    void test_Slack_BuildUndefinedStatus_slackSendIsNotExecuted(){
        Helper.setBuildStatus('UNDEFINED', slack_)
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}
        slack_.echo = { str -> return null}

        slack_(channel, allure)

        assertFalse(slackSendWasExecuted)

    }

    @Test
    void test_Slack_BuildUndefinedStatus_echoSentMessage(){
        Helper.setBuildStatus('UNDEFINED', slack_)
        def actualMessage = ""
        slack_.echo = { str -> actualMessage = str}
        def expectedMessage = 'slackSend is muted. Undefined build status: UNDEFINED'
        slack_(channel, allure)

        assertEquals(actualMessage, expectedMessage)

    }

}
