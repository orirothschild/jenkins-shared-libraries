import TestData.BuildResult
import TestData.Docker.DockerPushLatestTestData
import TestData.Docker.DockerTestData
import Utils.Exceptions.DockerRegistryIsNotDefinedException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerPushLatest_Variable_Tests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerTestData.customImageNames() +
        DockerTestData.defaultImageNames()
    }

    protected String imageName
    protected dockerPushLatest_ = new dockerPushLatest()

	DockerPushLatest_Variable_Tests(String imageName){
        this.imageName = imageName
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariablesWithoutDockerRegistry()
        Helper.setEnvVariables(variables, dockerPushLatest_)
        InjectVars.injectTo(dockerPushLatest_,'imageName', 'imageTag')
    }

    @Test
    void test_DockerPushLatest_DockerRegistryIsNotDefined_ErrorIsThrown(){
        def actualCommands = []
        dockerPushLatest_.sh = { command -> actualCommands << command; return null}
        dockerPushLatest_.error = { String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        thrown.expect(DockerRegistryIsNotDefinedException.class)
        thrown.expectMessage('Variable DOCKER_REGISTRY is not defined')
        dockerPushLatest_ imageName: imageName
    }

    @Test
    void test_DockerPushLatest_DockerRegistryIsNotDefined_shellCommandDockerBuildIsNotExecuted(){
        def actualCommands = []
        dockerPushLatest_.sh = { command -> actualCommands << command; return null}
        dockerPushLatest_.error = { String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        def expectedCommands = []

        try {
            dockerPushLatest_ imageName: imageName
            fail("Expected an DockerRegistryIsNotDefined to be thrown")
        }catch(DockerRegistryIsNotDefinedException e){
            assertEquals(expectedCommands, actualCommands)
        }
    }

}
