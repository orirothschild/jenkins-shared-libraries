import TestData.DockerBuildTestData
import Utils.Helper
import org.junit.Rule
import org.junit.Before
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_IncorrectDockerfilePathTests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerBuildTestData.incorrectPaths()
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none()

    protected String path
    protected dockerBuild_ = new dockerBuild()

    DockerBuild_IncorrectDockerfilePathTests(String path){
        this.path = path
    }

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariable(variables, dockerBuild_)
    }

    @Test
    void test_DockerBuild_exceptionInvalidPath(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        dockerBuild_.error = { message -> throw new TestInvalidPathException(message.toString())}
        def expectedMessage = "Invalid docker file path: ${path}".toString()
        thrown.expect(TestInvalidPathException.class)
        thrown.expectMessage(expectedMessage)

        dockerBuild_(path)

        assertEquals([], actualCommands)

    }

    @Test
    void test_DockerBuild_shellCommandDockerBuildIsNotExecuted(){
        def actualCommands = []
        dockerBuild_.sh = { command -> actualCommands << command; return null}
        dockerBuild_.error = { message -> throw new TestInvalidPathException(message.toString())}

        try {
            dockerBuild_(path)
            fail("Expected an TestInvalidPathException to be thrown")
        }catch(e){
            assertEquals([], actualCommands)
        }



    }

    class TestInvalidPathException extends Exception{
        TestInvalidPathException(String message){
            super(message)
        }
    }

}
