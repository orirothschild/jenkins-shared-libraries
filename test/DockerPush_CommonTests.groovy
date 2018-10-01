import TestData.Docker.DockerPushLatestTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerPush_CommonTests extends GroovyTestCase {

    protected dockerPush_ = new dockerPush()

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariables()
        Helper.setEnvVariable(variables, dockerPush_)
    }

    @Test
    void test_DockerPushLatest_NoParameters_checkOrderOfCommands(){
        def actualShellCommands = []
        dockerPush_.sh = { command -> actualShellCommands << command; return null}
        def expectedShellCommands = [
                'docker push registry.com/bilderlings/Job_Name:master-1'
        ]

        dockerPush_()

        assertEquals(expectedShellCommands, actualShellCommands)

    }

}
