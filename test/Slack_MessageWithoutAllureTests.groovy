import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class Slack_MessageWithoutAllureTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data(){
        SlackTestData.suite_ChannelIsDefined_AllureIsFalse() +
        SlackTestData.suite_ChannelIsEmpty_AllureIsFalse() +
        SlackTestData.suite_AllureIsFalse_ChannelIsNull() +
        SlackTestData.suite_ChannelIsWhitespace_AllureIsFalse()

    }

    private slack_ = new slack()
    private channel
    private allure

    Slack_MessageWithoutAllureTests(List list){
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
    void test_SlackSuccessMessageWithoutAllureTest_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(channel, allure)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackFailedMessageWithoutAllureTest_MessageIsCorrect(){
        Helper.setBuildStatus('FAILURE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build failed! (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(channel, allure)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackUnstableMessageWithoutAllureTest_MessageIsCorrect(){
        Helper.setBuildStatus('UNSTABLE', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name tests failed! (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(channel, allure)

        assertEquals(expectedMessage, actualParameters['message'])

    }

}
