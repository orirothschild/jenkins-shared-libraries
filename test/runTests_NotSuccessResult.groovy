import TestData.RunTestsData
import Utils.Exceptions.StageResultException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
class runTests_NotSuccessResult extends GroovyTestCase {

    protected runTests_ = new runTests()

    @Before
    void setUp(){
        def variables = RunTestsData.commonVariables()
        Helper.setEnvVariables(variables, runTests_)
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void test_RunTests_AbortedResult_errorWillBeRaised(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'ABORTED', fullDisplayName:'build #1']
        }
        runTests_.error = { msg -> throw new StageResultException(msg.toString()) }
        thrown.expect(StageResultException.class)
        thrown.expectMessage('build #1 completed with status ABORTED')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

    }

    @Test
    void test_RunTests_UnstableResult_errorWillBeRaised(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'UNSTABLE', fullDisplayName:'build #1']
        }
        runTests_.error = { msg -> throw new StageResultException(msg.toString()) }
        thrown.expect(StageResultException.class)
        thrown.expectMessage('build #1 completed with status UNSTABLE')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

    }

    @Test
    void test_RunTests_FailureResult_errorWillBeRaised(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'FAILURE', fullDisplayName:'build #1']
        }
        runTests_.error = { msg -> throw new StageResultException(msg.toString()) }
        thrown.expect(StageResultException.class)
        thrown.expectMessage('build #1 completed with status FAILURE')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

    }

}
