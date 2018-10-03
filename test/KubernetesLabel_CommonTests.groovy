import Utils.Helper
import org.junit.Test

class KubernetesLabel_CommonTests extends GroovyTestCase {

    protected kubernetesLabel_ = new kubernetesLabel()

    @Test
    void test_KubernetesLabel_BuildTagLengthIs53_SingleJobIsReturned(){
        Helper.setEnvVariables([BUILD_TAG: 'jenkins-pipeline-tests_dev-TXLU52ILA4PEHAEWP6ETC5YZOQ'], kubernetesLabel_)

        assertEquals('jenkins-pipeline-tests_dev-TXLU52ILA4PEHAEWP6ETC5YZOQ-x', kubernetesLabel_())

    }

    @Test
    void test_KubernetesLabel_BuildTagLengthIsLess53_SingleJobIsReturned(){
        Helper.setEnvVariables([BUILD_TAG: 'jenkins-pipeline-tests_dev-TXLU52ILA4PEHAEWP6ETC5YZO'], kubernetesLabel_)

        assertEquals('jenkins-pipeline-tests_dev-TXLU52ILA4PEHAEWP6ETC5YZO-x', kubernetesLabel_())

    }

    @Test
    void test_KubernetesLabel_BuildTagLengthIsMore53_SingleJobIsReturned(){
        Helper.setEnvVariables([BUILD_TAG: 'jenkins-pipeline-tests_dev-TXLU52ILA4PEHAEWP6ETC5YZOQB'], kubernetesLabel_)

        assertEquals('jenkins-pipeline-tests_dev-TXLU52ILA4PEHAEWP6ETC5YZOQ-x', kubernetesLabel_())

    }

}
