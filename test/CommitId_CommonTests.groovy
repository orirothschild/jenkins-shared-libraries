import TestData.CommitIdTestData
import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class CommitId_CommonTests extends GroovyTestCase {

    protected commitId_ = new commitId()

    @Before
    void setUp(){
        Helper.setEnvVariables([:], commitId_)
    }

    @Test
    void test_CommitId_CommitIdIsReturned(){

        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

}
