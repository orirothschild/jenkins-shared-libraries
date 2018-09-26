import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class SlackStatusTests extends GroovyTestCase {

    protected slack_ = new slack()

    @Before
    void setUp(){
        def slackVariables = SlackTestData.commonVariables()
        Helper.setEnvVariable(slackVariables, slack_)
    }

    @Test
    void test_SlackSuccessStatus_slackSendIsExecuted(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}

        slack_()

        assertTrue(slackSendWasExecuted)

    }

    @Test
    void test_SlackFailureStatus_slackSendIsExecuted(){
        Helper.setBuildStatus('FAILURE', slack_)
        Map actualParameters = [:]
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}

        slack_()

        assertTrue(slackSendWasExecuted)

    }

    @Test
    void test_SlackUnstableStatus_slackSendIsExecuted(){
        Helper.setBuildStatus('UNSTABLE', slack_)
        Map actualParameters = [:]
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}

        slack_()

        assertTrue(slackSendWasExecuted)

    }

    @Test
    void test_SlackUndefinedStatus_slackSendIsNotExecuted(){
        Helper.setBuildStatus('UNDEFINED', slack_)
        def slackSendWasExecuted = false
        slack_.slackSend = { Map map -> slackSendWasExecuted = true; return null}
        slack_.echo = { str -> return null}

        slack_()

        assertFalse(slackSendWasExecuted)

    }

    @Test
    void test_SlackUndefinedStatus_echoSentMessage(){
        Helper.setBuildStatus('UNDEFINED', slack_)
        def actualMessage = ""
        slack_.echo = { str -> actualMessage = str}
        def expectedMessage = 'slackSend is muted. Undefined build status: UNDEFINED'
        slack_()

        assertEquals(actualMessage, expectedMessage)

    }

}
