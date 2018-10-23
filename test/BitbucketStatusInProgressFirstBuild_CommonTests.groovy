import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test

class BitbucketStatusInProgressFirstBuild_CommonTests extends GroovyTestCase {

    protected bitbucketStatusInProgress_ = new bitbucketStatusInProgress()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatusInProgress_)
        InjectVars.injectTo(bitbucketStatusInProgress_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusInProgress_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusInProgressFirstBuild_StatusIsInProgress(){
        def actualParameters = [:]
        bitbucketStatusInProgress_.httpRequest = { Map map -> actualParameters = map; return null}
        def data = [
                state: 'INPROGRESS',
                url: 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build',
                key: 'Job_Name'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatusInProgress_()

        assertEquals(expectedBody, actualParameters['requestBody'])

    }

}
