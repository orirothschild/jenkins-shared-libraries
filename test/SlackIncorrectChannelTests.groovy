import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class SlackIncorrectChannelTests extends GroovyTestCase {


    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data(){
        SlackTestData.suite_ChannelIsEmpty_AllureIsCorrect() +
        SlackTestData.suite_ChannelIsWhitespace_AllureIsAny() +
        SlackTestData.suite_ChannelIsNull_AllureIsAny()
    }

    private slack_ = new slack()
    private channel
    private allure

    SlackIncorrectChannelTests(List list){
        this.channel = list[0]
        this.allure = list[1]
    }

    @Before
    void setUp(){
        def slackVariables = SlackTestData.commonVariables()
        Helper.setEnvVariable(slackVariables, slack_)
    }

    @Test
    void test_SlackSuccessIncorrectChannelTest_ChannelIsCorrect(){

        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_(channel, allure)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackFailedIncorrectChannelTest_ChannelIsCorrect(){

        Helper.setBuildStatus('FAILURE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_(channel, allure)

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_SlackUnstableIncorrectChannelTest_ChannelIsCorrect(){

        Helper.setBuildStatus('UNSTABLE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_(channel, allure)

        assertNull(actualParameters['channel'])

    }

}
