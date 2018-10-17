import TestData.Docker.DockerPushTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerPush_CommonTests extends GroovyTestCase {

    protected dockerPush_ = new dockerPush()

    @Before
    void setUp(){
        def variables = DockerPushTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerPush_)
        InjectVars.injectTo(dockerPush_, 'imageTag', 'imageName')
    }

    @Test
    void test_DockerPushLatest_NoParameters_checkOrderOfCommands(){
        def actualShellCommands = []
        dockerPush_.sh = { command -> actualShellCommands << command; return null}
        def expectedShellCommands = [
                'docker push \"registry.com/bilderlings/Job_Name:master-1\"'
        ]

        dockerPush_()

        assertEquals(expectedShellCommands, actualShellCommands)

    }

}
