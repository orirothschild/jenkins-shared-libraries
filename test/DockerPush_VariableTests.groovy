import TestData.Docker.DockerPushTestData
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
class DockerPush_VariableTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerTestData.customImageNames() +
        DockerTestData.defaultImageNames()
    }

    protected String imagename
    protected dockerPush_ = new dockerPush()

    DockerPush_VariableTests(String imageName){
        this.imagename = imageName
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    void setUp(){
        def variables = DockerPushTestData.commonVariablesWithoutDockerRegistry()
        Helper.setEnvVariables(variables, dockerPush_)
        InjectVars.injectTo(dockerPush_, 'imageName', 'commitId')
    }

    @Test
    void test_DockerPush_DockerRegistryIsNotDefined_ErrorIsThrown(){
        def actualCommands = []
        dockerPush_.sh = { command -> actualCommands << command; return null}
        dockerPush_.error = { String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        thrown.expect(DockerRegistryIsNotDefinedException.class)
        thrown.expectMessage('Variable DOCKER_REGISTRY is not defined')
        dockerPush_(imagename)
    }

    @Test
    void test_DockerPush_DockerRegistryIsNotDefined_shellCommandDockerBuildIsNotExecuted(){
        def actualCommands = []
        dockerPush_.sh = { command -> actualCommands << command; return null}
        dockerPush_.error = { String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        def expectedCommands = []

        try {
            dockerPush_(imagename)
            fail("Expected an DockerRegistryIsNotDefined to be thrown")
        }catch(DockerRegistryIsNotDefinedException e){
            assertEquals(expectedCommands, actualCommands)
        }
    }

}
