import TestData.BuildResult
import TestData.RunTestsData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class RunTests_NotSuccessResult extends GroovyTestCase {

    protected String testBuildStatus
    @Parameterized.Parameters(name = "{0}")
    static Collection data() {
        ['UNSTABLE', 'ABORTED', 'FAILURE']
    }

    RunTests_NotSuccessResult(String status){
        testBuildStatus = status
    }

    protected runTests_ = new runTests()

    @Before
    void setUp(){
        def variables = RunTestsData.commonVariables()
        Helper.setEnvVariables(variables, runTests_)
        InjectVars.injectTo(runTests_, 'successBuild')
    }

    @Test
    void test_RunTests_echoWillBeSend(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params -> RunTestsData.defaultBuildResult(testBuildStatus) }
        runTests_.currentBuild = new BuildResult()
        def actualMessage = ''
        runTests_.echo = { msg -> actualMessage = msg }
        def expectedMessage = "ERROR: build #1 completed with status ${testBuildStatus}".toString()

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(expectedMessage, actualMessage)

    }

    @Test
    void test_RunTests_AbortedResult_setBuildStatusToUnstable(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params -> RunTestsData.defaultBuildResult(testBuildStatus) }
        runTests_.currentBuild = new BuildResult()
        runTests_.echo = { }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('UNSTABLE', runTests_.currentBuild.currentResult)

    }

}
