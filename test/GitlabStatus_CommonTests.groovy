import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import groovy.json.JsonSlurper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class GitlabStatus_CommonTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        return [
                ['ABORTED', 'canceled'] as Object[],
                ['FAILURE', 'failed'] as Object[],
                ['NOT_BUILT', 'canceled'] as Object[],
                ['SUCCESS', 'success'] as Object[],
                ['UNSTABLE', 'failed'] as Object[],
        ]
    }

    protected Script gitlabStatus_ = new gitlabStatus()
    protected String jenkinsStatus
    protected String gitlabState

    GitlabStatus_CommonTests(String jenkinsStatus, String gitlabState) {
        this.jenkinsStatus = jenkinsStatus
        this.gitlabState = gitlabState
    }

    @Before
    void setUp() {
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, gitlabStatus_)
        InjectVars.injectTo(gitlabStatus_, 'commitId', 'TOKEN')
        InjectVars.injectClosureTo(gitlabStatus_, 'sh', ShellTestData.getShellClosure())
        InjectVars.injectClosureTo(gitlabStatus_, 'string', {})
        InjectVars.injectClosureTo(gitlabStatus_, 'withCredentials', {
            Object object, Closure closure -> closure()
        })
    }

    @Test
    void test_GitlabStatus() {
        Helper.setBuildStatus(this.jenkinsStatus, gitlabStatus_)

        def actualHttpRequestParameters = []
        gitlabStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        gitlabStatus_()

        assertTrue('We should have 1 request', actualHttpRequestParameters.size() == 1)

        def parameters = actualHttpRequestParameters[0]
        def jsonSlurper = new JsonSlurper()
        def data = jsonSlurper.parseText(parameters.requestBody)

        assertEquals(
                'https://gitlab.com/api/v4/projects/bilderlings%2Fjenkins-shared-libraries/statuses/1111111222222222222222222222222222222222',
                parameters['url']
        )
        assertEquals(
                [[name: 'private-token', value: '123']],
                parameters['customHeaders']
        )
        assertEquals(
                [
                        state     : this.gitlabState,
                        ref       : 'not_master',
                        name      : 'Jenkins #1',
                        target_url: 'http://jenkins.k8s.iamoffice.lv/job/test/1'
                ],
                data
        )
    }

    @Test
    void test_GitlabStatus_isRunning() {
        Helper.setBuildStatus(this.jenkinsStatus, gitlabStatus_)

        def actualHttpRequestParameters = []
        gitlabStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        gitlabStatus_(isRunning: true)

        assertTrue('We should have 1 request', actualHttpRequestParameters.size() == 1)

        def parameters = actualHttpRequestParameters[0]
        def jsonSlurper = new JsonSlurper()
        def data = jsonSlurper.parseText(parameters.requestBody)

        assertEquals(
                'https://gitlab.com/api/v4/projects/bilderlings%2Fjenkins-shared-libraries/statuses/1111111222222222222222222222222222222222',
                parameters['url']
        )
        assertEquals(
                [[name: 'private-token', value: '123']],
                parameters['customHeaders']
        )
        assertEquals(
                [
                        state     : 'running',
                        ref       : 'not_master',
                        name      : 'Jenkins #1',
                        target_url: 'http://jenkins.k8s.iamoffice.lv/job/test/1'
                ],
                data
        )
    }

}
