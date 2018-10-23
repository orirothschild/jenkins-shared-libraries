import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test

class BitbucketStatusFailedFirstBuild_CommonTests extends GroovyTestCase {

    protected bitbucketStatusFailed_ = new bitbucketStatusFailed()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatusFailed_)
        InjectVars.injectTo(bitbucketStatusFailed_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusFailed_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusFailedFirstBuild_StatusIsFailed(){
        def actualParameters = [:]
        bitbucketStatusFailed_.httpRequest = { Map map -> actualParameters = map; return null}

        def data = [
                state: 'FAILED',
                url: 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build',
                key: 'Job_Name'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatusFailed_()

        assertEquals(expectedBody, actualParameters['requestBody'])

    }

}
