import TestData.DockerPushLatestTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerPushLatest_Tests extends GroovyTestCase {

    protected dockerPushLatest_ = new dockerPushLatest()

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariables()
        Helper.setEnvVariable(variables, dockerPushLatest_)
    }

    @Test
    void test_DockerPushLatest_checkOrderOfCommands(){
        def actualShellCommands = []
        dockerPushLatest_.sh = { command -> actualShellCommands << command; return null}
        def expectedShellCommands = [
                'docker tag registry.com/bilderlings/Job_Name:master-1 registry.com/bilderlings/Job_Name:latest',
                'docker push registry.com/bilderlings/Job_Name:master-1',
                'docker push registry.com/bilderlings/Job_Name:latest',
        ]

        dockerPushLatest_()

        assertEquals(expectedShellCommands, actualShellCommands)

    }

}
