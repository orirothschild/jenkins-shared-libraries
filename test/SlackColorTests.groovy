import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class SlackColorTests extends GroovyTestCase {

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

    SlackColorTests(list){
        this.channel = channel
        this.allure = allure
    }

    @Before
    void setUp(){
        def slackVariables = SlackTestData.commonVariables()
        Helper.setEnvVariable(slackVariables, slack_)
    }

    @Test
    void test_SlackSuccessColorTest_ColorIsGood(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_(channel, allure)

        assertEquals('good', actualParameters['color'])

    }

    @Test
    void test_SlackFailedColorTest_ColorIsDanger(){
        Helper.setBuildStatus('FAILURE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_(channel, allure)

        assertEquals('danger', actualParameters['color'])

    }

    @Test
    void test_SlackUnstableColorTest_ColorIsWarning(){
        Helper.setBuildStatus('UNSTABLE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_(channel, allure)

        assertEquals('warning', actualParameters['color'])

    }

}
