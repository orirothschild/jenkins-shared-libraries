import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_DefinedBuildStatusCustomBitBucketStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_DefinedBuildStatus_CustomBitbucketStatuses()
    }

    protected String buildStatus
    protected String customBitbucketStatus

    BitbucketStatus_DefinedBuildStatusCustomBitBucketStatusTests(List list){
        this.buildStatus = list[0]
        this.customBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariable(variables, bitbucketStatus_)
        bitbucketStatus_.sh = { Map map ->
            if (map.returnStdout && map.script == "git log -n 1 --pretty=format:'%H'"){
                return "1111"
            }
            throw new Exception("Undefined shell parameters for getting last commit id: ${map}")
        }
    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_buildStateIsCustom(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}
        def expectedStatus = "${customBitbucketStatus}".toString()

        bitbucketStatus_(customBitbucketStatus)

        assertEquals(expectedStatus, actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_commitIdIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(customBitbucketStatus)

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(customBitbucketStatus)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }



}
