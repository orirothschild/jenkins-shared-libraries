import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_DefaultParametersUndefinedBuildStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()
    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        BitbucketStatusTestData.undefinedBuildStatuses()
    }

    protected String buildStatus

    BitbucketStatus_DefaultParametersUndefinedBuildStatusTests(String buildStatus){
        this.buildStatus = buildStatus
    }
    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariable(variables, bitbucketStatus_)
        bitbucketStatus_.sh = { Map map ->
            if (map.returnStdout && map.script == "git log -n 1 --pretty=format:'%H'"){
                return "1111"
            }
            throw new Exception("Undefined shell parameters for getting last commit id: ${map}")
        }
    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_bitbucketStatusNotifyIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_()

        assertFalse(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_echoSentMessage(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualMessage = ""
        bitbucketStatus_.echo = { str -> actualMessage = str}
        def expectedMessage = "bitbucketStatusNotify is muted. Undefined build status: ${buildStatus}".toString()
        bitbucketStatus_()

        assertEquals(expectedMessage, actualMessage)

    }

}
