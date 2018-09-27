import TestData.DockerBuildTestData
import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerPush_CorrectDockerfilePathTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerBuildTestData.correctPaths()
    }

    protected path
    protected slack_ = new slack()

    DockerPush_CorrectDockerfilePathTests(String path){
        this.path = path
    }

    @Before
    void setUp(){
        def slackVariables = SlackTestData.commonVariables()
        Helper.setEnvVariable(slackVariables, slack_)
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
