import TestData.SuccessBuildData
import org.junit.Test

class SuccessBuild_Tests extends GroovyTestCase {

    def successBuild_ = new successBuild()

    @Test
    void test_SuccessBuild_CurrentBuildIsSuccess_returnTrue(){

        successBuild_.currentBuild = SuccessBuildData.successBuildResult()

        assertTrue(successBuild_())

    }

    @Test
    void test_SuccessBuild_CurrentBuildIsUnstable_returnFalse(){

        successBuild_.currentBuild = SuccessBuildData.unstableBuildResult()

        assertFalse(successBuild_())

    }

    @Test
    void test_SuccessBuild_CurrentBuildIsFailure_returnFalse(){

        successBuild_.currentBuild = SuccessBuildData.failureBuildResult()

        assertFalse(successBuild_())

    }

    @Test
    void test_SuccessBuild_CurrentBuildIsAborted_returnFalse(){

        successBuild_.currentBuild = SuccessBuildData.abortedBuildResult()

        assertFalse(successBuild_())

    }

    @Test
    void test_SuccessBuild_CurrentBuildIsUnknown_returnFalse(){

        successBuild_.currentBuild = SuccessBuildData.unknownBuildResult()

        assertFalse(successBuild_())

    }

    @Test
    void test_SuccessBuild_AnotherBuildIsSuccess_returnTrue(){

        assertTrue(successBuild_(SuccessBuildData.successBuildResult()))

    }

    @Test
    void test_SuccessBuild_AnotherBuildIsUnstable_returnFalse(){

        assertFalse(successBuild_(SuccessBuildData.unstableBuildResult()))

    }

    @Test
    void test_SuccessBuild_AnotherBuildIsFailure_returnFalse(){

        assertFalse(successBuild_(SuccessBuildData.failureBuildResult()))

    }

    @Test
    void test_SuccessBuild_AnotherBuildIsAborted_returnFalse(){

        assertFalse(successBuild_(SuccessBuildData.abortedBuildResult()))

    }

    @Test
    void test_SuccessBuild_AnotherBuildIsUnknown_returnFalse(){

        assertFalse(successBuild_(SuccessBuildData.unknownBuildResult()))

    }

}
