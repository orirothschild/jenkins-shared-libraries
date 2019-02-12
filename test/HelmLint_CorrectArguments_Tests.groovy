import TestData.HelmLintTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class HelmLint_CorrectArguments_Tests extends GroovyTestCase {

    def helmLint_ = new helmLint()
    String namespace
    Map args
    List resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmLintTestData.suite_correctNamespaceCorrectArgsCases()
    }

	HelmLint_CorrectArguments_Tests(Map map){
        namespace = map.namespace
        args = map.args
        resultCommand = map.result
    }

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmLintTestData.commonVariables(), helmLint_)
    }

    @Test
    void test_HelmLint_CorrectArguments_shellIsExecuted(){

        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script.contains('mktemp /tmp/helm_lint_log.XXXXXX')) {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint') || command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmLint_.echo = { String msg -> }

        helmLint_ namespace: namespace, set: args

        assertEquals(5, actualCommands.size())
        assertEquals('mktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals(resultCommand[0] + ' &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('cat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals(resultCommand[1], actualCommands[3])
        assertEquals('rm /tmp/helm_lint_log.1111111', actualCommands[4])
    }

    @Test
    void test_HelmLint_CorrectArguments_StdOutAndStdErrInEchoMessages(){

        helmLint_.sh = { command ->
            if (command instanceof Map) {
                if (command.returnStdout) {
                    if (command.script.contains('mktemp /tmp/helm_lint_log.XXXXXX')) {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint') || command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
            }
        }
        def messages = []
        helmLint_.echo = { String msg -> messages << msg}

        helmLint_ namespace: namespace, set: args

        assertEquals(1, messages.size())
        assertEquals('[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error', messages[0])
    }

}
