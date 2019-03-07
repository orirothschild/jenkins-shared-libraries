import TestData.BuildResult
import TestData.HelmLintTestData
import Utils.Exceptions.HelmLintException
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class HelmLint_LintAndTemplateError_Tests extends GroovyTestCase {

    def helmLint_ = new helmLint()
    String namespace
    Map args
    List resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmLintTestData.suite_correctNamespaceCorrectArgsCases()
    }

    HelmLint_LintAndTemplateError_Tests(Map map){
        namespace = map.namespace
        args = map.args
        resultCommand = map.result
    }

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmLintTestData.commonVariables(), helmLint_)
        InjectVars.injectTo(helmLint_, 'imageName')
        helmLint_.currentBuild = new BuildResult()
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWithErrorMessage_errorIsThrown(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }
        try {
            helmLint_ namespace: namespace, set: args
            fail('No HelmLintException was thrown')
        }catch(HelmLintException ex){
            assertEquals('Helm lint exit code 1\n[ERROR]: error', ex.message)
        }
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWithoutErrorMessage_errorIsThrown(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return ''
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }
        try {
            helmLint_ namespace: namespace, set: args
            fail('No HelmLintException was thrown')
        }catch(HelmLintException ex){
            assertEquals('Helm lint exit code 1', ex.message)
        }
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWithErrorMessageAndWarningError_errorIsThrown(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }
        try {
            helmLint_ namespace: namespace, set: args
            fail('No HelmLintException was thrown')
        }catch(HelmLintException ex){
            assertEquals('Helm lint exit code 1\n[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error', ex.message)
        }
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWithErrorMessageAndWarningError_buildStatusIsError(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }
        try {
            helmLint_ namespace: namespace, set: args
            fail('No HelmLintException was thrown')
        }catch(HelmLintException ex){
            assertEquals('FAILURE', helmLint_.currentBuild.currentResult)
        }
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWithErrorMessageAndWarningError_shellIsExecuted(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }
        try {
            helmLint_ namespace: namespace, set: args
            fail('No HelmLintException was thrown')
        }catch(HelmLintException ex){
            assertEquals(5, actualCommands.size())
            assertEquals('mktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
            assertEquals(resultCommand[0] + ' &>/tmp/helm_lint_log.1111111', actualCommands[1])
            assertEquals('cat /tmp/helm_lint_log.1111111', actualCommands[2])
            assertEquals(resultCommand[1], actualCommands[3])
            assertEquals('rm /tmp/helm_lint_log.1111111', actualCommands[4])
        }
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWarningError_errorIsNotThrown(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[Info]: info'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }

        helmLint_ namespace: namespace, set: args

        assertEquals(5, actualCommands.size())
        assertEquals('mktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals(resultCommand[0] + ' &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('cat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals(resultCommand[1], actualCommands[3])
        assertEquals('rm /tmp/helm_lint_log.1111111', actualCommands[4])
    }

    @Test
    void test_HelmLint_ErrorWithHelmLintWarningError_IgnoringTemplateError(){
        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == 'cat /tmp/helm_lint_log.1111111'){
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[Info]: info'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint')){
                        return 1
                    }else if (command.script.startsWith('helm template')){
                        return 1
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmLint_.echo = { String msg -> }
        helmLint_.error = { String msg ->
            throw new HelmLintException(msg.toString())
        }

        helmLint_ namespace: namespace, set: args

        assertEquals(5, actualCommands.size())
        assertEquals('mktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals(resultCommand[0] + ' &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('cat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals(resultCommand[1], actualCommands[3])
        assertEquals('rm /tmp/helm_lint_log.1111111', actualCommands[4])
    }

}
