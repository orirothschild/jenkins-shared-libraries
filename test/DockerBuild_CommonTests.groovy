import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerBuild_CommonTests extends GroovyTestCase {

    protected dockerBuild_ = new dockerBuild()

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariable(variables, dockerBuild_)
    }

    @Test
    void test_DockerBuild_NoParameters_DefaultParameters(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ['docker build . -t registry.com/bilderlings/Job_Name:master-1 -f .']

        dockerBuild_()

        assertEquals(expectedCommands, actualCommands)

    }

}
