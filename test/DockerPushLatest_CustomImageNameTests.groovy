import TestData.Docker.DockerPushLatestTestData
import TestData.Docker.DockerTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerPushLatest_CustomImageNameTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerTestData.customImageNames()
    }

    protected String imageName
    protected dockerPushLatest_ = new dockerPushLatest()

    DockerPushLatest_CustomImageNameTests(String imageName){
        this.imageName = imageName
    }

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerPushLatest_)
        InjectVars.injectTo(dockerPushLatest_, 'imageTag', 'imageName')
    }

    @Test
    void test_DockerPushLatest_checkOrderOfCommands(){
        def actualShellCommands = []
        dockerPushLatest_.sh = { command -> actualShellCommands << command; return null}
        def expectedShellCommands = [
                "docker tag \"registry.com/bilderlings/${imageName}:master-1\" \"registry.com/bilderlings/${imageName}:latest\"".toString(),
                "docker push \"registry.com/bilderlings/${imageName}:latest\"".toString(),
        ]

        dockerPushLatest_(imageName)

        assertEquals(expectedShellCommands, actualShellCommands)

    }

}
