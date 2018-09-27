import TestData.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_CorrectDockerfilePathTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerBuildTestData.correctPaths()
    }

    protected String path
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_CorrectDockerfilePathTests(String path){
        this.path = path
    }

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariable(variables, dockerBuild_)
    }

    @Test
    void test_DockerBuild_shellCommandDockerBuildIsExecuted(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ["docker build . -t registry.com/bilderlings/Job_Name:master-1 -f ${path}".toString()]

        dockerBuild_(path)

        assertEquals(expectedCommands, actualCommands)

    }

}
