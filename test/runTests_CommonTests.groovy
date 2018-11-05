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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(jobName, buildParams['job'])

    }

    @Test
    void test_RunTests_propagateIsTrue(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.string = { Map stringProp ->
            stringProps <<  stringProp
            stringProp
        }
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(true, buildParams['propagate'])

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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

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
            [buildVariables: [BUILD_URL: "http://localhost:8080/job/child/1234/"]]
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(1, buildIsExecuted)

    }

}
