import TestData.HelmUpgradeTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class HelmUpgrade_ArgsTests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()
    String namespace
    Map args
    def resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmUpgradeTestData.suite_correctNamespaceCorrectArgsCases()
    }

    HelmUpgrade_ArgsTests(Map map){
        namespace = map.namespace
        args = map.args
        resultCommand = map.result
    }

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmUpgradeTestData.commonVariables(), helmUpgrade_)
        InjectVars.injectTo(helmUpgrade_, 'imageName')
    }

    @Test
    void test_HelmUpgrade_Args_shellIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script.contains('mktemp /tmp/helm_upgrade_stderr.XXXXXX')) {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmUpgrade_.echo = {String msg -> }
        helmUpgrade_(namespace, args)

        assertEquals(3, actualCommands.size())
        assertEquals(resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
    }

    @Test
    void test_HelmUpgradeMap_Args_shellIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script.contains('mktemp /tmp/helm_upgrade_stderr.XXXXXX')) {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmUpgrade_.echo = {String msg -> }
        helmUpgrade_ namespace: namespace, set: args

        assertEquals(3, actualCommands.size())
        assertEquals(actualCommands[1], resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111')
    }


}
