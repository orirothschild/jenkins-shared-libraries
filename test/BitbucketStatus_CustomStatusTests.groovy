import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_CustomStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        BitbucketStatusTestData.customBitbucketStatuses()
    }

    protected String status

    BitbucketStatus_CustomStatusTests(String status){
        this.status = status
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
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}
        def expectedStatus = "${status}".toString()

        bitbucketStatus_(status)

        assertEquals(expectedStatus, actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_commitIdIsCorrect(){
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(status)

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_BitbucketCustomStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(status)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_BuildFailureStatus_BitbucketCustomStatus_buildStateIsCustom(){
        Helper.setBuildStatus('FAILURE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}
        def expectedStatus = "${status}".toString()

        bitbucketStatus_(status)

        assertEquals(expectedStatus, actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildFailureStatus_BitbucketCustomStatus_commitIdIsCorrect(){
        Helper.setBuildStatus('FAILURE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(status)

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildFailureStatus_BitbucketCustomStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus('FAILURE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(status)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_BuildUnstableStatus_BitbucketCustomStatus_buildStateIsCustom(){
        Helper.setBuildStatus('UNSTABLE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}
        def expectedStatus = "${status}".toString()

        bitbucketStatus_(status)

        assertEquals(expectedStatus, actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildUnstableStatus_BitbucketCustomStatus_commitIdIsCorrect(){
        Helper.setBuildStatus('UNSTABLE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(status)

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildUnstableStatus_BitbucketCustomStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus('UNSTABLE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(status)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_BitbucketCustomStatus_bitbucketStatusNotifyIsExecuted(){
        Helper.setBuildStatus('UNDEFINED', bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_(status)

        assertTrue(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_BitbucketCustomStatus_echoDoNotSentMessage(){
        Helper.setBuildStatus('UNDEFINED', bitbucketStatus_)
        def echoIsExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> null}
        bitbucketStatus_.echo = { str -> echoIsExecuted = true; return null }
        bitbucketStatus_(status)

        assertFalse(echoIsExecuted)

    }

}
