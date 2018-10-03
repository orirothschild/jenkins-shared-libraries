import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_DefinedBuildStatusesDefaultBitbucketStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_DefinedBuildStatus_DefaultBitbucketStatuses()
    }

    protected String defaultBitbucketStatus
    protected String buildStatus

    BitbucketStatus_DefinedBuildStatusesDefaultBitbucketStatusTests(List list){
        this.buildStatus = list[0]
        this.defaultBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_buildStateIsCustom(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(defaultBitbucketStatus)

        assertEquals(BitbucketStatusTestData.buildStateMap()[buildStatus], actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_commitIdIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(defaultBitbucketStatus)

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(defaultBitbucketStatus)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

}
