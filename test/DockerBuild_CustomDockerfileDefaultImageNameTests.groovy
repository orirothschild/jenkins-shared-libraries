import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_CustomDockerfileDefaultImageNameTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        DockerBuildTestData.suite_CustomDockerFilePathsWithDefaultImageNames()
    }

    protected String path
    protected String imagename
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_CustomDockerfileDefaultImageNameTests(List list){
        this.path = list[0]
        this.imagename = list[1]
    }

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerBuild_)
        InjectVars.injectTo( dockerBuild_,'imageName', 'imageTag')
    }

    @Test
    void test_DockerBuild_shellCommandDockerBuildIsExecuted(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        def expectedCommands = ["docker build . -t registry.com/bilderlings/Job_Name:master-1 -f ${path}".toString()]

        dockerBuild_(path, imagename)

        assertEquals(expectedCommands, actualCommands)

    }

}
