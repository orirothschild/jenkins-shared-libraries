import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatusSuccessful_CommonTests extends GroovyTestCase {

    protected bitbucketStatusSuccessful_ = new bitbucketStatusSuccessful()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatusSuccessful_)
        InjectVars.injectTo(bitbucketStatusSuccessful_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusSuccessful_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusFailed_StatusIsFailed(){
        def actualParameters = [:]
        bitbucketStatusSuccessful_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusSuccessful_()

        assertEquals('SUCCESSFUL', actualParameters['buildState'])

    }

}
