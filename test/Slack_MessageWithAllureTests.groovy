import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class Slack_MessageWithAllureTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data(){
        SlackTestData.suite_ChannelIsDefined_AllureIsTrue() +
        SlackTestData.suite_ChannelIsNull_AllureIsTrue() +
        SlackTestData.suite_ChannelIsEmpty_AllureIsTrue() +
        SlackTestData.suite_ChannelIsWhitespace_AllureIsTrue()

    }

    private slack_ = new slack()
    private channel
    private allure

    Slack_MessageWithAllureTests(List list){
        this.channel = list[0]
        this.allure = list[1]
    }

    @Before
    void setUp(){
        def variables = SlackTestData.commonVariables()
        Helper.setEnvVariable(variables, slack_)
    }

    @Test
    void test_SlackSuccessMessageWithAllureTest_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <FAKE_Build_Urlallure/|Allure report> (<FAKE_Build_Url|1234>)'

        slack_(channel, allure)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackFailedMessageWithAllureTest_MessageIsCorrect(){
        Helper.setBuildStatus('FAILURE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build failed! <FAKE_Build_Urlallure/|Allure report> (<FAKE_Build_Url|1234>)'

        slack_(channel, allure)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackUnstableMessageWithAllureTest_MessageIsCorrect(){
        Helper.setBuildStatus('UNSTABLE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name tests failed! <FAKE_Build_Urlallure/|Allure report> (<FAKE_Build_Url|1234>)'

        slack_(channel, allure)

        assertEquals(expectedMessage, actualParameters['message'])

    }

}
