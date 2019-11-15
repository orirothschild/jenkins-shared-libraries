import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class Slack_CommonTests extends GroovyTestCase {

    private slack_ = new slack()

    @Before
    void setUp(){
        def variables = SlackTestData.commonVariables()
        Helper.setEnvVariables(variables, slack_)
        InjectVars.injectTo(slack_, 'imageName')
    }

    @Test
    void test_Slack_DefaultParameters_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! (<http://jenkins.k8s.iamoffice.lv/job/FAKE_Job_Name/job/FAKE_Branch_Name/1234/|1234>)'

        slack_()

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_Slack_DefaultParameters_ChannelIsNull(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_()

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_Slack_MapParametersAllureTrue_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <FAKE_Build_Urlallure/|build-tests> (<http://jenkins.k8s.iamoffice.lv/job/FAKE_Job_Name/job/FAKE_Branch_Name/1234/|1234>)'

        slack_ channel: "#channel", allure: true

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_Slack_MapParametersAllureFalse_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! (<http://jenkins.k8s.iamoffice.lv/job/FAKE_Job_Name/job/FAKE_Branch_Name/1234/|1234>)'

        slack_ channel: "#channel", allure: false

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_Slack_MapParameters_ChannelIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_ channel: "#channel", allure: true

        assertEquals("#channel", actualParameters['channel'])

    }

    @Test
    void test_Slack_MapParametersIsNull_DefaultValuesAreApplied_ChannelIsNull(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        slack_ null

        assertNull(actualParameters['channel'])

    }

    @Test
    void test_Slack_MapParametersIsNull_DefaultValuesAreApplied_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! (<http://jenkins.k8s.iamoffice.lv/job/FAKE_Job_Name/job/FAKE_Branch_Name/1234/|1234>)'

        slack_ null

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_Slack_MapParametersBlueOceanFalse_MessageIsCorrect(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}
        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! (<http://jenkins.k8s.iamoffice.lv/job/FAKE_Job_Name/job/FAKE_Branch_Name/1234/|1234>)'

        slack_ channel: "#channel", allure: false, blueocean: false

        assertEquals(expectedMessage, actualParameters['message'])

    }

}
