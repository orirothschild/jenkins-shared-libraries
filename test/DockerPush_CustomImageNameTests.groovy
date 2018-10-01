import TestData.Docker.DockerPushLatestTestData
import TestData.Docker.DockerTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerPush_CustomImageNameTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerTestData.customImageNames()
    }

    protected String imageName
    protected dockerPush_ = new dockerPush()

    DockerPush_CustomImageNameTests(String imageName){
        this.imageName = imageName
    }

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariables()
        Helper.setEnvVariable(variables, dockerPush_)
    }

    @Test
    void test_DockerPushLatest_checkOrderOfCommands(){
        def actualShellCommands = []
        dockerPush_.sh = { command -> actualShellCommands << command; return null}
        def expectedShellCommands = [
                "docker push registry.com/bilderlings/${imageName}:master-1".toString()
        ]

        dockerPush_(imageName)

        assertEquals(expectedShellCommands, actualShellCommands)

    }

}
