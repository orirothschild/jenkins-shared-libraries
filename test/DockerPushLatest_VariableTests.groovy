import TestData.Docker.DockerBuildTestData
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
class DockerPushLatest_VariableTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerTestData.customImageNames() +
        DockerTestData.defaultImageNames()
    }

    protected String imagename
    protected dockerPushLatest_ = new dockerPushLatest()

    DockerPushLatest_VariableTests(String imageName){
        this.imagename = imageName
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariablesWithoutDockerRegistry()
        Helper.setEnvVariable(variables, dockerPushLatest_)
    }

    @Test
    void test_DockerPushLatest_DockerRegistryIsNotDefined_ErrorIsThrown(){
        def actualCommands = []
        dockerPushLatest_.sh = { command -> actualCommands << command; return null}
        dockerPushLatest_.error = { String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        thrown.expect(DockerRegistryIsNotDefinedException.class)
        thrown.expectMessage('Variable DOCKER_REGISTRY is not defined')
        dockerPushLatest_(imagename)
    }

    @Test
    void test_DockerPushLatest_DockerRegistryIsNotDefined_shellCommandDockerBuildIsNotExecuted(){
        def actualCommands = []
        dockerPushLatest_.sh = { command -> actualCommands << command; return null}
        dockerPushLatest_.error = { String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        def expectedCommands = []

        try {
            dockerPushLatest_(imagename)
            fail("Expected an DockerRegistryIsNotDefined to be thrown")
        }catch(DockerRegistryIsNotDefinedException e){
            assertEquals(expectedCommands, actualCommands)
        }
    }
}
