import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Exceptions.InvalidBitbucketStatusException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_UndefinedBuildStatusesInvalidBitbucketStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_UndefinedBuildStatus_InvalidBitbucketStatuses()
    }

    protected String invalidBitbucketStatus
    protected String buildStatus

    BitbucketStatus_UndefinedBuildStatusesInvalidBitbucketStatusTests(List list){
        this.buildStatus = list[0]
        this.invalidBitbucketStatus = list[1]
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_BitbucketCustomStatus_bitbucketStatusNotifyIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        bitbucketStatus_.error = { msg -> throw new InvalidBitbucketStatusException(msg.toString()) }
        thrown.expect(InvalidBitbucketStatusException.class)
        thrown.expectMessage("Undefined bitbucket status: ${invalidBitbucketStatus}".toString())

        bitbucketStatus_(invalidBitbucketStatus)

    }

}
