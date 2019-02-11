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
    void test_Deploy_RunTestsIsCalledOnce() {
        deploy_.helmUpgrade = {return null}
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
    void test_Deploy_ParallelIsCalled() {
        deploy_.helmUpgrade = {return null}
        def parallelParameters = [:]
        deploy_.parallel = {Map map -> parallelParameters = map; return null}
        Map firstJob = [
            'job': 'test/master-1',
            'parameters': ['test-param-1']
        ]
        Map secondJob = [
            'job': 'test/master-2',
            'parameters': ['test-param-2']
        ]
        deploy_("test", [:], [firstJob, secondJob])
        assertEquals(parallelParameters.size(), 2)
        assertTrue(parallelParameters.containsKey('test/master-1'))
        assertTrue(parallelParameters['test/master-1'] instanceof Closure)
        assertTrue(parallelParameters.containsKey('test/master-2'))
        assertTrue(parallelParameters['test/master-2'] instanceof Closure)
    }

}