import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatusFailed_CommonTests extends GroovyTestCase {

    protected bitbucketStatusFailed_ = new bitbucketStatusFailed()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatusFailed_)
        InjectVars.injectTo(bitbucketStatusFailed_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusFailed_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusFailed_StatusIsFailed(){
        def actualParameters = [:]
        bitbucketStatusFailed_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusFailed_()

        assertEquals('FAILED', actualParameters['buildState'])

    }

}
