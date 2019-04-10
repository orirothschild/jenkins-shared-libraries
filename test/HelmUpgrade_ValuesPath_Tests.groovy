import TestData.HelmUpgradeTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class HelmUpgrade_ValuesPath_Tests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmUpgradeTestData.commonVariables(), helmUpgrade_)
        InjectVars.injectTo(helmUpgrade_, 'imageName')
    }

    @Test
    void test_HelmUpgradeMap_ValuesPathNull_CustomValuesWithNamespace(){

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

        helmUpgrade_ namespace: 'any', valuesPath: null

        assertEquals(3, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
        assertEquals('helm upgrade -f "chart/values-any.yaml" --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/ 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_upgrade_stderr.1111111', actualCommands[2])

    }

    @Test
    void test_HelmUpgradeMap_ValuesPathEmpty_NoCustomValues(){

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

        helmUpgrade_ namespace: 'any', valuesPath: ''

        assertEquals(3, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
        assertEquals('helm upgrade --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/ 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_upgrade_stderr.1111111', actualCommands[2])

    }

    @Test
    void test_HelmUpgradeMap_ValuesPathWhitespace_NoCustomValues(){

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

        helmUpgrade_ namespace: 'any', valuesPath: ' '

        assertEquals(3, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
        assertEquals('helm upgrade --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/ 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_upgrade_stderr.1111111', actualCommands[2])

    }

    @Test
    void test_HelmUpgradeMap_ValuesPathIsSpecified_CustomValues(){

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

        helmUpgrade_ namespace: 'any', valuesPath: './some-values.yaml'

        assertEquals(3, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_upgrade_stderr.XXXXXX', actualCommands[0])
        assertEquals('helm upgrade -f "./some-values.yaml" --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/ 2>/tmp/helm_upgrade_stderr.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_upgrade_stderr.1111111', actualCommands[2])

    }

}
