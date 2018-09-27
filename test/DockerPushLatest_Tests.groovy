import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerBuild_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Before
    void setUp(){
        def bitbucketVariables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariable(bitbucketVariables, bitbucketStatus_)
        bitbucketStatus_.sh = { Map map ->
            if (map.returnStdout && map.script == "git log -n 1 --pretty=format:'%H'"){
                return "1111"
            }
            throw new Exception("Undefined shell parameters for getting last commit id: ${map}")
        }
    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_buildStateIsSuccessful(){
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('SUCCESSFUL', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_commitIdIsCorrect(){
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildSuccessStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus('SUCCESS', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_BuildFailureStatus_buildStateIsFailed(){
        Helper.setBuildStatus('FAILURE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('FAILED', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildFailureStatus_commitIdIsCorrect(){
        Helper.setBuildStatus('FAILURE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}


        bitbucketStatus_()

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildFailureStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus('FAILURE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_BuildUnstableStatus_buildStateIsFailed(){
        Helper.setBuildStatus('UNSTABLE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('FAILED', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_BuildUnstableStatus_commitIdIsCorrect(){
        Helper.setBuildStatus('UNSTABLE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_BuildUnstableStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus('UNSTABLE', bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_bitbucketStatusNotifyIsNotExecuted(){
        Helper.setBuildStatus('UNDEFINED', bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_()

        assertFalse(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatus_BuildUndefinedStatus_echoSentMessage(){
        Helper.setBuildStatus('UNDEFINED', bitbucketStatus_)
        def actualMessage = ""
        bitbucketStatus_.echo = { str -> actualMessage = str}
        def expectedMessage = 'bitbucketStatusNotify is muted. Undefined build status: UNDEFINED'
        bitbucketStatus_()

        assertEquals(actualMessage, expectedMessage)

    }

}
