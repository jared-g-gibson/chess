package clientTests;

import model.UserData;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.LoginRequest;
import response.CreateGameResponse;
import response.LoginResponse;
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

    @Test
    public void loginPass() throws Exception {
        facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        LoginResponse response = facade.loginUser(new LoginRequest("player1", "password"));
        assertTrue(response.getAuthToken().length() == 36);
    }

    @Test
    public void loginFail() throws Exception {
        facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        LoginResponse response = facade.loginUser(new LoginRequest("player1", "wrongPassword"));
        assertEquals("Error: unauthorized", response.getMessage());
    }

    @Test
    public void createGamePass() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", null));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        // Created game successfully
        assertNull(gameResponse.getMessage());
    }

    /*@Test
    public void createGameFail() {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", null));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        // Created game successfully
        assertNull(gameResponse.getMessage());
    }*/


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

}
