import TestData.BuildResult
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
class DockerBuild_Variable_Tests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        DockerBuildTestData.suite_CustomDockerFilePathsWithCustomImageNames() +
        DockerBuildTestData.suite_CustomDockerFilePathsWithDefaultImageNames() +
        DockerBuildTestData.suite_DefaultDockerFilePathsWithCustomImageNames() +
        DockerBuildTestData.suite_DefaultDockerFilePathsWithDefaultImageNames()
    }

    protected String path
    protected String imageName
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_Variable_Tests(List list){
        this.path = list[0]
        this.imageName = list[1]
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariablesWithoutDockerRegistry()
        Helper.setEnvVariables(variables, dockerBuild_)
        InjectVars.injectTo(dockerBuild_, 'imageName', 'imageTag', 'commitId')
        dockerBuild_.currentBuild = new BuildResult()
    }

    @Test
    void test_DockerBuild_DockerRegistryIsNotDefined_ErrorIsThrown(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        dockerBuild_.error = {String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        thrown.expect(DockerRegistryIsNotDefinedException.class)
        thrown.expectMessage('Variable DOCKER_REGISTRY is not defined')
        dockerBuild_ dockerfile: path, imagename: imageName
    }

    @Test
    void test_DockerBuild_DockerRegistryIsNotDefined_shellCommandDockerBuildIsNotExecuted(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        dockerBuild_.error = {String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        def expectedCommands = []

        try {
            dockerBuild_ dockerfile: path, imageName: imageName
            fail("Expected an DockerRegistryIsNotDefined to be thrown")
        }catch(DockerRegistryIsNotDefinedException e){
            assertEquals(expectedCommands, actualCommands)
        }
    }

    @Test
    void test_DockerBuild_DockerRegistryIsNotDefined_buildResultIsFailure(){
        dockerBuild_.sh = { command -> return null}
        dockerBuild_.error = {String msg -> throw new DockerRegistryIsNotDefinedException(msg) }
        try {
            dockerBuild_ dockerfile: path, imageName: imageName
            fail("Expected an DockerRegistryIsNotDefined to be thrown")
        }catch(DockerRegistryIsNotDefinedException e){
            assertEquals("FAILURE", dockerBuild_.currentBuild.currentResult)
        }
    }

}
