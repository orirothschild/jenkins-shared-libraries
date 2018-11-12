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
class HelmUpgrade_ErrorAndRollbackTests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()
    String namespace
    Map args
    def resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmUpgradeTestData.suite_correctNamespaceCorrectArgsCases()
    }

    HelmUpgrade_ErrorAndRollbackTests(Map map){
        namespace = map.namespace
        args = map.args
        resultCommand = map.result
    }

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmUpgradeTestData.commonVariables(), helmUpgrade_)
        InjectVars.injectTo(helmUpgrade_, 'imageName')
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void test_HelmUpgrade_Exception_rollbackIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            actualCommands << command
            if (((String)command).startsWith('helm upgrade')){
                throw new Exception('HELM UPGRADE error')
            }
        }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        def expectedCommand = 'helm rollback "FAKE_Job_Name-test" 0'
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals(2, actualCommands.size())
            assertEquals(expectedCommand, actualCommands[1])
        }
    }

    @Test
    void test_HelmUpgrade_Exception_errorWithMessageIsThrown(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            actualCommands << command
            if (((String)command).startsWith('helm upgrade')){
                throw new Exception('HELM UPGRADE error')
            }
        }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}

        thrown.expect(HelmUpgradeException.class)
        thrown.expectMessage('HELM UPGRADE error')
        helmUpgrade_(namespace, args)
    }

}
