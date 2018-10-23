import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()
    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        BitbucketStatusTestData.definedBuildStatuses()
    }

    protected String buildStatus

    BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_Tests(String buildStatus){
        this.buildStatus = buildStatus
    }
    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageName', 'commitId')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_requestUrlIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        def expectedUrl = 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build'
        bitbucketStatus_()

        assertEquals(expectedUrl, actualParameters['url'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_authenticationIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('bitbucket-oauth-credentials', actualParameters['authentication'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_httpMethodIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('POST', actualParameters['httpMode'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_contentTypeIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('APPLICATION_JSON', actualParameters['contentType'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_responseCodeIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('200:201', actualParameters['validResponseCodes'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefaultCallParameters_DefinedBuildStatus_requestBodyIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.httpRequest = { Map map -> actualParameters = map; return null}

        def data = [
                state: BitbucketStatusTestData.buildStateMap()[buildStatus],
                url: 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111/statuses/build',
                key: 'Job_Name'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatus_()

        assertEquals(expectedBody, actualParameters['requestBody'])

    }

}
