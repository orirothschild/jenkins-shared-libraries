import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_DefaultDockerfileCustomImageNameTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        DockerBuildTestData.suite_DefaultDockerFilePathsWithCustomImageNames()
    }

    protected String path
    protected String imageName
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_DefaultDockerfileCustomImageNameTests(List list){
        this.path = list[0]
        this.imageName = list[1]
    }

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerBuild_)
        InjectVars.injectTo(dockerBuild_, 'imageName', 'imageTag')
    }

    @Test
    void test_DockerBuild_shellCommandDockerBuildIsExecutedWithDefaultPath(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ["docker build . -t \"registry.com/bilderlings/${imageName}:master-1\" -f \"./Dockerfile\"".toString()]

        dockerBuild_(path, imageName)

        assertEquals(expectedCommands, actualCommands)

    }

}
