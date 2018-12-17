import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class BitbucketStatusInProgress_CommonTests extends GroovyTestCase {

    protected bitbucketStatusInProgress_ = new bitbucketStatusInProgress()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatusInProgress_)
        InjectVars.injectTo(bitbucketStatusInProgress_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusInProgress_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatusInProgress_StatusIsInProgress(){
        def actualParameters = [:]
        bitbucketStatusInProgress_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusInProgress_()

        assertEquals('INPROGRESS', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatusInProgress_NoInProgressStatus_StatusIsInProgress(){
        def actualParameters = [:]
        bitbucketStatusInProgress_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusInProgress_ status: 'NotInProgress'

        assertEquals('INPROGRESS', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatusInProgress_SetRepoSlug_RepoSlugIsChanged(){
        def actualParameters = [:]
        bitbucketStatusInProgress_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusInProgress_ repoSlug: 'custom_repo'

        assertEquals('custom_repo', actualParameters['repoSlug'])

    }

}
