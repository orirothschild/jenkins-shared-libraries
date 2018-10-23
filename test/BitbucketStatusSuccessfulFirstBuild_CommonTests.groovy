import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test

class BitbucketStatusSuccessfulFirstBuild_CommonTests extends GroovyTestCase {

    protected bitbucketStatusSuccessful_ = new bitbucketStatusSuccessful()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatusSuccessful_)
        InjectVars.injectTo(bitbucketStatusSuccessful_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusSuccessful_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusFailed_StatusIsFailed(){
        def actualParameters = [:]
        bitbucketStatusSuccessful_.httpRequest = { Map map -> actualParameters = map; return null}
        def data = [
                state: 'SUCCESSFUL',
                url: 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build',
                key: 'Job_Name'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatusSuccessful_()

        assertEquals(expectedBody, actualParameters['requestBody'])

    }

}
