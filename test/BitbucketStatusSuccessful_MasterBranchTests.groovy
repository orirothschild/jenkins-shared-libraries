import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatusSuccessful_MasterBranchTests extends GroovyTestCase {

    protected bitbucketStatusSuccessful_ = new bitbucketStatusSuccessful()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuildMaster()
        Helper.setEnvVariables(variables, bitbucketStatusSuccessful_)
        InjectVars.injectTo(bitbucketStatusSuccessful_, 'bitbucketStatus')
    }

    @Test
    void test_BitbucketStatus_MasterBranch_OnlyEchoIsExecuted(){
        def httpRequestIsExecuted = false
        bitbucketStatusSuccessful_.httpRequest = { Map map ->
            httpRequestIsExecuted = true
        }
        def notifyIsExecuted = false
        bitbucketStatusSuccessful_.bitbucketStatusNotify = { Map map ->
            notifyIsExecuted = true
        }
        def echoMessage = ''
        bitbucketStatusSuccessful_.echo = { msg ->
            echoMessage = msg?.toString()
        }

        bitbucketStatusSuccessful_()

        assertFalse('httpRequest is not executed', httpRequestIsExecuted)
        assertFalse('bitbucketStatusNotify is not executed',  notifyIsExecuted)
        assertEquals('Bitbucket status is ignored cause \'master\' branch', echoMessage)

    }

}
