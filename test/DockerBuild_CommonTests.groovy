import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerBuild_CommonTests extends GroovyTestCase {

    protected dockerBuild_ = new dockerBuild()

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerBuild_)
        InjectVars.injectTo(dockerBuild_, 'imageName', 'imageTag')
    }

    @Test
    void test_DockerBuild_NoParameters_DefaultParameters(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ['docker build . -t \"registry.com/bilderlings/Job_Name:master-1\" -f \"./Dockerfile\"']

        dockerBuild_()

        assertEquals(expectedCommands, actualCommands)

    }

    @Test
    void test_DockerBuild_MapParameter_ParametersAreDispatched(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ['docker build . -t \"registry.com/bilderlings/imagename:master-1\" -f \"path\"']

        dockerBuild_ dockerfile: 'path', imageName: 'imagename'

        assertEquals(expectedCommands, actualCommands)

    }

    @Test
    void test_DockerBuild_NullParameter_DefaultParameters(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ['docker build . -t \"registry.com/bilderlings/Job_Name:master-1\" -f \"./Dockerfile\"']

        dockerBuild_ null

        assertEquals(expectedCommands, actualCommands)

    }

}
