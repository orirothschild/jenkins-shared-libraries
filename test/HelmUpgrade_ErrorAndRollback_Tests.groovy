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
class HelmUpgrade_ErrorAndRollback_Tests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()
    String namespace
    Map args
    def resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmUpgradeTestData.suite_correctNamespaceCorrectArgsCases()
    }

    HelmUpgrade_ErrorAndRollback_Tests(Map map){
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
    void test_HelmUpgrade_ErrorWithHelmUpgradeMessage_rollbackIsExecuted(){
        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return 'UPGRADE FAILED: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 1
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmUpgrade_.echo = {String msg -> }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals(5, actualCommands.size())
            assertEquals('mktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
            assertEquals(resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
            assertEquals('cat /tmp/helm_upgrade_stderr.1111111', actualCommands[2])
            assertEquals('helm rollback --wait "FAKE_Job_Name-test" 0', actualCommands[3])
            assertEquals('rm /tmp/helm_upgrade_stderr.1111111', actualCommands[4])
        }
    }

    @Test
    void test_HelmUpgrade_ErrorWithHelmUpgradeMessage_errorIsThrown(){
        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return 'UPGRADE FAILED: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 157
                    }else if (command.script.startsWith('helm rollback')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmUpgrade_.echo = {String msg -> }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals('Exit code 157\nUPGRADE FAILED: error', ex.message)
        }
    }

    @Test
    void test_HelmUpgrade_ErrorWithHelmUpgradeMessageAndRollbackError_errorIsThrown(){
        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return 'UPGRADE FAILED: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 157
                    }else if (command.script.startsWith('helm rollback')){
                        return 111
                    }
                }
            } else{
                actualCommands << command
            }
        }

        helmUpgrade_.echo = {String msg -> }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals('Exit code 157\nUPGRADE FAILED: error\nRollback failed. Exit code 111', ex.message)
        }
    }

    @Test
    void test_HelmUpgrade_ErrorWithNoHelmUpgradeMessage_rollbackIsNotExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return 'error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 1
                    } else if (command.script.startsWith('helm rollback')){
                        return 0
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        helmUpgrade_.echo = {}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals(4, actualCommands.size())
            assertEquals('mktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
            assertEquals(resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
            assertEquals('cat /tmp/helm_upgrade_stderr.1111111', actualCommands[2])
            assertEquals('rm /tmp/helm_upgrade_stderr.1111111', actualCommands[3])
        }
    }

    @Test
    void test_HelmUpgrade_ErrorWithNoHelmUpgradeMessage_errorIsThrown(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return 'error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 157
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        helmUpgrade_.echo = {}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals('Exit code 157\nerror', ex.message)
        }
    }

    @Test
    void test_HelmUpgrade_ErrorWithNoHelmUpgradeMessage_messageIsSent(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return 'error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 1
                    }
                }
            } else{
                actualCommands << command
            }
        }
        def actualMessage = ''
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        helmUpgrade_.echo = {msg -> actualMessage = msg.toString(); return null}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals("It seems not a upgrading failure. If it's the failure, you can do 'helm rollback --wait \"FAKE_Job_Name-test\" 0'", actualMessage)
        }
    }


    @Test
    void test_HelmUpgrade_ErrorWithoutMessage_rollbackIsNotExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return ''
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 1
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        helmUpgrade_.echo = {}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals(4, actualCommands.size())
            assertEquals('mktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
            assertEquals(resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
            assertEquals('cat /tmp/helm_upgrade_stderr.1111111', actualCommands[2])
            assertEquals('rm /tmp/helm_upgrade_stderr.1111111', actualCommands[3])
        }
    }


    @Test
    void test_HelmUpgrade_ErrorWithoutMessage_errorIsThrownOnlyWithExitCode(){

        def actualCommands = []
        helmUpgrade_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == 'mktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    } else if (command.script == 'cat /tmp/helm_upgrade_stderr.1111111'){
                        return ''
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 157
                    }
                }
            } else{
                actualCommands << command
            }
        }
        helmUpgrade_.error = {msg -> throw new HelmUpgradeException(msg.toString())}
        helmUpgrade_.echo = {}
        try {
            helmUpgrade_(namespace, args)
            fail('No HelmUpgradeException was thrown')
        }catch(HelmUpgradeException ex){
            assertEquals('Exit code 157', ex.message)
        }
    }


}
