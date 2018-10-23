import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_DefinedBuildStatus_ValidBitbucketStatuses()
    }

    protected String buildStatus
    protected String validBitbucketStatus

    BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_Tests(List list){
        this.buildStatus = list[0]
        this.validBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageName', 'commitId')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_requestUrlIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}
        def expectedUrl = 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build'

        bitbucketStatus_(validBitbucketStatus)

        assertEquals(expectedUrl, actualParameters['url'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_authenticationIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus)

        assertEquals('bitbucket-oauth-credentials', actualParameters['authentication'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_httpMethodIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus)

        assertEquals('POST', actualParameters['httpMode'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_contentTypeIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus)

        assertEquals('APPLICATION_JSON', actualParameters['contentType'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_responseCodeIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus)

        assertEquals('200:201', actualParameters['validResponseCodes'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_requestBodyIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        def data = [
                state: "${validBitbucketStatus}".toString(),
                url: 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build',
                key: 'Job_Name'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatus_(validBitbucketStatus)

        assertEquals(expectedBody, actualParameters['requestBody'])

    }

}
