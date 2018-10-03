import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_UndefinedBuildStatusCustomBitBucketStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_UndefinedBuildStatus_CustomBitbucketStatuses()
    }

    protected String buildStatus
    protected String customBitbucketStatus

    BitbucketStatus_UndefinedBuildStatusCustomBitBucketStatusTests(List list){
        this.buildStatus = list[0]
        this.customBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageTag', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_BitbucketCustomStatus_bitbucketStatusNotifyIsExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_(customBitbucketStatus)

        assertTrue(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_BitbucketCustomStatus_echoDoNotSentMessage(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def echoIsExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> null}
        bitbucketStatus_.echo = { str -> echoIsExecuted = true; return null }
        bitbucketStatus_(customBitbucketStatus)

        assertFalse(echoIsExecuted)

    }

}
