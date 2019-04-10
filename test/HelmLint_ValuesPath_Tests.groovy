import TestData.HelmLintTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class HelmLint_ValuesPath_Tests extends GroovyTestCase {

    def helmLint_ = new helmLint()

    @Before
    void setUp() {
        Helper.setEnvVariables(HelmLintTestData.commonVariables(), helmLint_)
    }

    @Test
    void test_HelmLint_ValuesPathNull_CustomValuesWithNamespace() {

        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == '#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111') {
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint') || command.script.startsWith('helm template')) {
                        return 0
                    }
                }
            } else {
                actualCommands << command
            }
        }
        helmLint_.echo = { String msg -> }

        helmLint_ namespace: 'any', valuesPath: null

        assertEquals(5, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals('helm lint -f "chart/values-any.yaml" --namespace "any" chart/ &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals('helm template -f "chart/values-any.yaml" --namespace "any" chart/', actualCommands[3])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_lint_log.1111111', actualCommands[4])
    }

    @Test
    void test_HelmLint_ValuesPathEmpty_NoCustomValues() {

        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == '#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111') {
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint') || command.script.startsWith('helm template')) {
                        return 0
                    }
                }
            } else {
                actualCommands << command
            }
        }
        helmLint_.echo = { String msg -> }

        helmLint_ namespace: 'any', valuesPath: ''

        assertEquals(5, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals('helm lint --namespace "any" chart/ &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals('helm template --namespace "any" chart/', actualCommands[3])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_lint_log.1111111', actualCommands[4])
    }

    @Test
    void test_HelmLint_ValuesPathWhitespace_NoCustomValues() {

        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == '#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111') {
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint') || command.script.startsWith('helm template')) {
                        return 0
                    }
                }
            } else {
                actualCommands << command
            }
        }
        helmLint_.echo = { String msg -> }

        helmLint_ namespace: 'any', valuesPath: ' '

        assertEquals(5, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals('helm lint --namespace "any" chart/ &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals('helm template --namespace "any" chart/', actualCommands[3])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_lint_log.1111111', actualCommands[4])
    }

    @Test
    void test_HelmLint_ValuesPathIsSpecified_CustomValues() {

        def actualCommands = []
        helmLint_.sh = { command ->
            if (command instanceof Map) {
                actualCommands << command.script
                if (command.returnStdout) {
                    if (command.script == '#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX') {
                        return "/tmp/helm_lint_log.1111111"
                    } else if (command.script == '#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111') {
                        return '[ERROR] Chart.yaml: directory name (chart) and chart name (site) must be the same\n[ERROR]: error'
                    }
                } else if (command.returnStatus) {
                    if (command.script.startsWith('helm lint') || command.script.startsWith('helm template')) {
                        return 0
                    }
                }
            } else {
                actualCommands << command
            }
        }
        helmLint_.echo = { String msg -> }

        helmLint_ namespace: 'any', valuesPath: './some-values.yaml'

        assertEquals(5, actualCommands.size())
        assertEquals('#!/bin/sh -e\nmktemp /tmp/helm_lint_log.XXXXXX', actualCommands[0])
        assertEquals('helm lint -f "./some-values.yaml" --namespace "any" chart/ &>/tmp/helm_lint_log.1111111', actualCommands[1])
        assertEquals('#!/bin/sh -e\ncat /tmp/helm_lint_log.1111111', actualCommands[2])
        assertEquals('helm template -f "./some-values.yaml" --namespace "any" chart/', actualCommands[3])
        assertEquals('#!/bin/sh -e\nrm /tmp/helm_lint_log.1111111', actualCommands[4])
    }
}