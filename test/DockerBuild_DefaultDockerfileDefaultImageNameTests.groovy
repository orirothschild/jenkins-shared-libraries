import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_DefaultDockerfileDefaultImageNameTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        DockerBuildTestData.suite_DefaultDockerFilePathsWithDefaultImageNames()
    }

    protected String path
    protected String imageName
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_DefaultDockerfileDefaultImageNameTests(List list){
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
        def expectedCommands = ['docker build . -t \"registry.com/bilderlings/Job_Name:master-1\" -f \"./Dockerfile\"']

        dockerBuild_(path, imageName)

        assertEquals(expectedCommands, actualCommands)

    }

}
