import TestData.SlackTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class Slack_CustomAllureReports extends GroovyTestCase {

    protected slack_ = new slack()

    @Before
    void setUp(){
        def variables = SlackTestData.commonVariables()
        Helper.setEnvVariables(variables, slack_)
        InjectVars.injectTo(slack_, 'imageName')

    }

    @Test
    void test_SlackOneCustomAllureReportDefaultAllureFalse_MessageContatinsOneAllureReport(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Helper.addEnvVariables(['account-tests-api_TESTS_URL': 'http://jenkins_build/job/job_name/1/allure'], slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <http://jenkins_build/job/job_name/1/allure|account-tests-api> (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(allure: false)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackOneCustomAllureReportDefaultAllureTrue_MessageContatinsOneAllureReport(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Helper.addEnvVariables(['account-tests-api_TESTS_URL': 'http://jenkins_build/job/job_name/1/allure'], slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <FAKE_Build_Urlallure/|build_tests> <http://jenkins_build/job/job_name/1/allure|account-tests-api> (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(allure: true)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackTwoCustomAllureReportDefaultAllureFalse_MessageContatinsTwoAllureReport(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Helper.addEnvVariables(['account-tests-api_TESTS_URL': 'http://jenkins_build/job/job_name/1/allure',
                                'account-tests-web_TESTS_URL': 'http://jenkins_build/job/job_name/1/allure'],
                                slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <http://jenkins_build/job/job_name/1/allure|account-tests-api> <http://jenkins_build/job/job_name/1/allure|account-tests-web> (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(allure: false)

        assertEquals(expectedMessage, actualParameters['message'])

    }

    @Test
    void test_SlackTwoCustomAllureReportDefaultAllureTrue_MessageContatinsTwoAllureReport(){
        Helper.setBuildStatus('SUCCESS', slack_)
        Helper.addEnvVariables(['account-tests-api_TESTS_URL': 'http://jenkins_build/job/job_name/1/allure',
                                'account-tests-web_TESTS_URL': 'http://jenkins_build/job/job_name/1/allure'],
                                slack_)
        Map actualParameters = [:]
        slack_.slackSend = { Map map -> actualParameters = map; return null}

        def expectedMessage = 'FAKE_Job_Name branch FAKE_Branch_Name build passed! <FAKE_Build_Urlallure/|build_tests> <http://jenkins_build/job/job_name/1/allure|account-tests-api> <http://jenkins_build/job/job_name/1/allure|account-tests-web> (<http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/FAKE_Job_Name/detail/FAKE_Branch_Name/1234/pipeline/|1234>)'

        slack_(allure: true)

        assertEquals(expectedMessage, actualParameters['message'])

    }

}
