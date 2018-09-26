import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class SlackMessageWithAllureTests extends GroovyTestCase {

    private slack_ = new slack()
    @Parameterized.Parameters
    static Collection<Object[]> data(){
        def parameters =
                SlackTestData.suiteParameterAllureTrueChannelCorrect() +
                SlackTestData.suiteParameterAllureTrueChannelEmpty() +
                SlackTestData.suiteParameterAllureTrueChannelNull() +
                SlackTestData.suiteParameterAllureTrueChannelWhitespace()

        return parameters

    }

    private channel
    private allure

    SlackMessageWithAllureTests(List list){
        this.channel = list[0]
        this.allure = list[1]
    }

    @Before
    void setUp(){
        def slackVariables = SlackTestData.commonVariables()
        Helper.setEnvVariable(slackVariables, slack_)
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
