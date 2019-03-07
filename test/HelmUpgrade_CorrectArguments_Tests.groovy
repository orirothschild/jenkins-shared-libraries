import TestData.HelmUpgradeTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class HelmUpgrade_CorrectArguments_Tests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()
    String namespace
    Map args
    def resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmUpgradeTestData.suite_correctNamespaceCorrectArgsCases()
    }

    HelmUpgrade_CorrectArguments_Tests(Map map){
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
    void test_HelmUpgrade_CorrectArguments_shellIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX') {
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
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
        assertEquals(resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_upgrade_stderr.1111111', actualCommands[2])
    }

    @Test
    void test_HelmUpgrade_CorrectArguments_NoEchoMessages(){

        helmUpgrade_.sh = {command ->
            if (command instanceof Map) {
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX') {
                        return "/tmp/helm_upgrade_stderr.1111111"
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm upgrade')){
                        return 0
                    }
                }
            } else{
            }
        }
        def messages = []
        helmUpgrade_.echo = {String msg -> messages << msg}

        helmUpgrade_(namespace, args)

        assertEquals(0, messages.size())
    }

    @Test
    void test_HelmUpgradeMap_CorrectArguments_shellIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX') {
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
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
        assertEquals(resultCommand + ' 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_upgrade_stderr.1111111', actualCommands[2])


    }

}
