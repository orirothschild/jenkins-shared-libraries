import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatus_MasterBranchTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuildMaster()
        Helper.setEnvVariables(variables, bitbucketStatus_)
    }

    @Test
    void test_BitbucketStatus_MasterBranch_OnlyEchoIsExecuted(){
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def httpRequestIsExecuted = false
        bitbucketStatus_.httpRequest = { Map map ->
            httpRequestIsExecuted = true
        }
        def notifyIsExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map ->
            notifyIsExecuted = true
        }
        def echoMessage = ''
        bitbucketStatus_.echo = { msg ->
            echoMessage = msg?.toString()
        }

        bitbucketStatus_()

        assertFalse('httpRequest is not executed', httpRequestIsExecuted)
        assertFalse('bitbucketStatusNotify is not executed',  notifyIsExecuted)
        assertEquals('Bitbucket status \'SUCCESS\' is ignored cause \'master\' branch', echoMessage)

    }

}
