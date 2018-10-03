import Utils.Helper
import org.junit.Test

class ImageTag_CommonTests extends GroovyTestCase {

    protected imageTag_ = new imageTag()

    @Test
    void test_ImageTag_ImageTagIsReturned(){

        Helper.setEnvVariables([BRANCH_NAME:'branch', BUILD_ID: '1234'], imageTag_)

        assertEquals('branch-1234', imageTag_())

    }

}
