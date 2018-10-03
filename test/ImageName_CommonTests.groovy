import Utils.Helper
import org.junit.Test

class ImageName_CommonTests extends GroovyTestCase {

    protected imageName_ = new imageName()

    @Test
    void test_ImageName_SingleJobName_SingleJobIsReturned(){
        Helper.setEnvVariables([JOB_NAME: 'JobName'], imageName_)

        assertEquals('JobName', imageName_())

    }

    @Test
    void test_ImageName_CompositeJobName_SingleJobIsReturned(){
        Helper.setEnvVariables([JOB_NAME: 'JobName/master'], imageName_)

        assertEquals('JobName', imageName_())

    }
}
