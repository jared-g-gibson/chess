package clientTests;

import model.UserData;
import org.junit.jupiter.api.*;
import response.RegisterResponse;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clear() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPass() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "p1@email.com"));
        assertNull(response.getMessage());
        assertTrue(response.getAuthToken().length() == 36);
    }

    @Test
    public void registerFail() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", null));
        assertEquals("Error: bad request", response.getMessage());
    }

    public void loginPass() throws Exception {

    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

}
