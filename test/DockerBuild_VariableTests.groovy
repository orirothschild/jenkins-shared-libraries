import TestData.Docker.DockerBuildTestData
import Utils.Exceptions.DockerRegistryIsNotDefinedException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_VariableTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        DockerBuildTestData.suite_CustomDockerFilePathsWithCustomImageNames() +
        DockerBuildTestData.suite_CustomDockerFilePathsWithDefaultImageNames() +
        DockerBuildTestData.suite_DefaultDockerFilePathsWithCustomImageNames() +
        DockerBuildTestData.suite_DefaultDockerFilePathsWithDefaultImageNames()
    }

    protected String path
    protected String imagename
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_VariableTests(List list){
        this.path = list[0]
        this.imagename = list[1]
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariablesWithoutDockerRegistry()
        Helper.setEnvVariable(variables, dockerBuild_)
    }

    @Test
    void test_DockerBuild_DockerRegistryIsNotDefined_ErrorIsThrown(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        dockerBuild_.error = {String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        thrown.expect(DockerRegistryIsNotDefinedException.class)
        thrown.expectMessage('Variable DOCKER_REGISTRY is not defined')
        dockerBuild_(path, imagename)
    }

    @Test
    void test_DockerBuild_DockerRegistryIsNotDefined_shellCommandDockerBuildIsNotExecuted(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        dockerBuild_.error = {String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        def expectedCommands = []

        try {
            dockerBuild_(path, imagename)
            fail("Expected an DockerRegistryIsNotDefined to be thrown")
        }catch(DockerRegistryIsNotDefinedException e){
            assertEquals(expectedCommands, actualCommands)
        }
    }

}