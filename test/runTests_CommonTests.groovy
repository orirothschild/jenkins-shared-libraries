import TestData.RunTestsData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class runTests_CommonTests extends GroovyTestCase {

    protected runTests_ = new runTests()

    @Before
    void setUp(){
        def variables = RunTestsData.commonVariables()
        Helper.setEnvVariables(variables, runTests_)
    }

    @Test
    void test_RunTestsOneStringParameter_StringParameterWillBeAdded(){
        def jobName = 'account-tests-api/master'
        def expectedStringObjs = [[name: 'TAG1', value: 'tag1']]
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAG1': 'tag1'])

        assertEquals(expectedStringObjs, stringProps)

    }
    @Test
    void test_RunTestsTwoStringParameter_StringParameterWillBeAdded(){
        def jobName = 'account-tests-api/master'
        def expectedStringObjs = [[name: 'TAG1', value: 'tag1'], [name: 'TAG2', value: 'tag2']]
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAG1': 'tag1', 'TAG2': 'tag2'])

        assertEquals(expectedStringObjs, stringProps)

    }

    @Test
    void test_RunTests_EnvironmentVariableWithUrlExist(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('http://localhost:8080/job/child/1234/allure/', runTests_.env['account-tests-api_TESTS_URL'])

    }

    @Test
    void test_RunTests_jobNameIsCorrect(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(jobName, buildParams['job'])

    }

    @Test
    void test_RunTests_propagateIsFalse(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(false, buildParams['propagate'])

    }

    @Test
    void test_RunTests_waitIsTrue(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(true, buildParams['wait'])

    }

    @Test
    void test_RunTests_parametersIsCorrect(){
        def jobName = 'account-tests-api/master'
        def expectedStringObjs = [[name: 'TAG1', value: 'tag1']]
        runTests_.string = { Map stringProp ->
            stringProp
        }
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAG1': 'tag1'])

        assertEquals(expectedStringObjs, buildParams['parameters'])

    }

    @Test
    void test_RunTests_buildExecutedOneTime(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        def buildIsExecuted = 0
        runTests_.build = {Map params ->
            buildIsExecuted++
            [absoluteUrl: "http://localhost:8080/job/child/1234/"]
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(1, buildIsExecuted)

    }

    @Test
    void test_RunTests_SuccessResult_childBuildResultWillNotBeSet(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'SUCCESS']
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('', runTests_.currentBuild.result)

    }

    @Test
    void test_RunTests_FailureResult_childBuildResultWillBeSetToUnstable(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'FAILURE']
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('FAILURE', runTests_.currentBuild.result)

    }

    @Test
    void test_RunTests_AbortedResult_childBuildResultWillBeSetToUnstable(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'ABORTED']
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('FAILURE', runTests_.currentBuild.result)

    }

    @Test
    void test_RunTests_UnstableResult_childBuildResultWillBeSetToUnstable(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        runTests_.build = {Map params ->
            [absoluteUrl: "http://localhost:8080/job/child/1234/", result: 'UNSTABLE']
        }
        runTests_.currentBuild = new Expando(result: '', currentResult: '')

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('FAILURE', runTests_.currentBuild.result)

    }

}
