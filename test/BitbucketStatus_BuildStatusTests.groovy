import TestData.BitbucketStatusTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatus_BuildStatusTests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Before
    void setUp(){
        def bitbucketVariables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariable(bitbucketVariables, bitbucketStatus_)
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

        assertEquals('image', actualParameters['repoSlug'])

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

        assertEquals('image', actualParameters['repoSlug'])

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

        assertEquals('image', actualParameters['repoSlug'])

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
