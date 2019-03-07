import TestData.BuildResult
import TestData.HelmUpgradeTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class Deploy_CommonTests extends GroovyTestCase {

    protected deploy_ = new deploy()

    @Before
    void setUp(){
        def variables = HelmUpgradeTestData.commonVariables()
        Helper.setEnvVariables(variables, deploy_)
        InjectVars.injectTo(deploy_, 'commitId', 'imageName')
        deploy_.lock = {Map map, Closure body -> body.call(); return null}
        deploy_.helmLint = {return null}
        deploy_.helmUpgrade = {return null}
        deploy_.milestone = {return null}
    }

    @Test
    void test_Deploy_LockIsCalled() {
        def lockParameters = [:]
        deploy_.lock = {Map map, Closure body -> lockParameters = map; return null}
        deploy_.imageName = {return "foo"}
        deploy_("test")
        assertEquals(lockParameters['resource'], "test-foo")
    }

    @Test
    void test_Deploy_MilestoneIsCalled() {
        def milestoneCalled = false
        deploy_.milestone = {milestoneCalled = true; return null}
        deploy_("test")
        assertTrue(milestoneCalled)
    }

    @Test
    void test_Deploy_HelmUpgradeIsCalled() {
        def helmParameters = [:]
        deploy_.helmUpgrade = { Map map -> helmParameters = map; return null}
        deploy_(
            "test",
            [
                'testKey': "testValue"
            ]
        )
        assertEquals(helmParameters['namespace'], "test")
        assertEquals(helmParameters['set'], [
            'testKey': "testValue"
        ])
    }

    @Test
    void test_Deploy_SanitizeThenRunThrowsErrorIfJobNameMissing() {
        def errorMsg = ""
        def runTestsCalled = false
        deploy_.runTests = {runTestsCalled = true; return null}
        deploy_.currentBuild = new BuildResult()
        deploy_.error = {msg -> errorMsg = (String)msg; return null}
        deploy_.sanitizeThenRun([:])
        assertEquals(errorMsg, "job name missing")
        assertFalse(runTestsCalled)
    }

    @Test
    void test_Deploy_RunTestsIsCalledOnceIfOnlyOneJobProvided() {
        def runParameters = [:]
        def runCount = 0
        deploy_.runTests = {Map map -> runParameters = map; runCount ++; return null}
        Map singleJob = [
            'job': 'test/master',
            'parameters': [
                    "test-param"
            ]
        ]
        deploy_("test", [:], [singleJob])
        assertEquals(runCount, 1)
        assertEquals(runParameters['job'], singleJob['job'])
        assertEquals(runParameters['parameters'], singleJob['parameters'])
    }

    @Test
    void test_Deploy_ParallelIsCalledIfMoreThenOneJobProvided() {
        def parallelParameters = [:]
        deploy_.parallel = {Map map -> parallelParameters = map; return null}
        def jobs = [
            [
                'job': 'test/master-0',
                'parameters': ['test-param-0']
            ],
            [
                'job': 'test/master-1',
                'parameters': ['test-param-1']
            ]
        ]
        deploy_("test", [:], jobs)
        assertEquals(parallelParameters.size(), jobs.size())
        jobs.each{ job ->
            assertTrue(parallelParameters.containsKey(job['job']))
            assertTrue(parallelParameters[job['job']] instanceof Closure)
            def callable = (Closure)parallelParameters[job['job']]
            def runParameters = [:]
            callable.runTests = {Map map -> runParameters = map; return null}
            callable.call()
            assertEquals(runParameters['job'], job['job'])
            assertEquals(runParameters['parameters'], job['parameters'])
        }
    }

}