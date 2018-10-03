import TestData.CommitIdTestData
import org.junit.Test

class CommitId_CommonTests extends GroovyTestCase {

    protected commitId_ = new commitId()

    @Test
    void test_CommitId_CommitIdIsReturned(){

        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

}
