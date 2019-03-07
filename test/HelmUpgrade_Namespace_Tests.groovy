import TestData.BuildResult
import TestData.HelmUpgradeTestData
import Utils.Exceptions.HelmUpgradeException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class HelmUpgrade_Namespace_Tests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()
    String namespace
    Map args

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmUpgradeTestData.suite_incorrectNamespaceCorrectArgsCases()
    }

    HelmUpgrade_Namespace_Tests(Map map){
        namespace = map.namespace
        args = map.args
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmUpgradeTestData.commonVariables(), helmUpgrade_)
        InjectVars.injectTo(helmUpgrade_, 'imageName')
    }

    @Test
    void test_HelmUpgrade_IncorrectNamespace_shellIsNotExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command -> actualCommands << command}
        helmUpgrade_.error = { String msg -> throw new HelmUpgradeException(msg) }
        try{
            helmUpgrade_(namespace, args)
            fail('Expected an HelmUpgradeException to be thrown')
        }catch(HelmUpgradeException e){
            assertEquals([], actualCommands)
        }
    }

    @Test
    void test_HelmUpgrade_IncorrectNamespace_exceptionWillThrownWithMessage(){

        def actualCommands = []
        helmUpgrade_.sh = {command -> actualCommands << command}
        helmUpgrade_.error = { String msg -> throw new HelmUpgradeException(msg) }
        thrown.expect(HelmUpgradeException.class)
        thrown.expectMessage("Undefined namespace: ${namespace}".toString())

        helmUpgrade_(namespace, args)
    }

    @Test
    void test_HelmUpgradeMap_IncorrectNamespace_shellIsNotExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command -> actualCommands << command}
        helmUpgrade_.error = { String msg -> throw new HelmUpgradeException(msg) }
        try{
            helmUpgrade_ namespace: namespace, set: args
            fail('Expected an HelmUpgradeException to be thrown')
        }catch(HelmUpgradeException e){
            assertEquals([], actualCommands)
        }
    }

    @Test
    void test_HelmUpgradeMap_IncorrectNamespace_exceptionWillThrownWithMessage(){

        def actualCommands = []
        helmUpgrade_.sh = {command -> actualCommands << command}
        helmUpgrade_.error = { String msg -> throw new HelmUpgradeException(msg) }
        thrown.expect(HelmUpgradeException.class)
        thrown.expectMessage("Undefined namespace: ${namespace}".toString())

        helmUpgrade_ namespace: namespace, set: args
    }
}
