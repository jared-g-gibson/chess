package clientTests;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.*;
import response.*;
import server.Server;
import server.ServerFacade;

import java.util.ArrayList;

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
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        // You have to log out since registering automatically logs you in
        facade.logoutUser(new LogoutRequest(response.getAuthToken()));

        // Check if authToken is 36 characters long
        facade.loginUser(new LoginRequest("player1", "password"));
        assertTrue(response.getAuthToken().length() == 36);
    }

    @Test
    public void loginFail() throws Exception {
        facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        LoginResponse response = facade.loginUser(new LoginRequest("player1", "wrongPassword"));
        assertEquals("Error: unauthorized", response.getMessage());
    }

    @Test
    public void logoutPass() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        LogoutResponse logoutResponse = facade.logoutUser(new LogoutRequest(response.getAuthToken()));
        // Logged out successfully
        Assertions.assertNull(logoutResponse.getMessage());
    }

    @Test
    public void logoutFail() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        LogoutResponse logoutResponse = facade.logoutUser(new LogoutRequest("Wrong authToken"));
        // Logged out successfully
        Assertions.assertEquals("Error: unauthorized", logoutResponse.getMessage());
    }

    @Test
    public void createGamePass() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        // Created game successfully
        assertNull(gameResponse.getMessage());
    }

    @Test
    public void createGameFail() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", null));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), "Wrong auth");
        // Created game successfully
        assertNotNull(gameResponse.getMessage());
        assertEquals("Error: unauthorized", gameResponse.getMessage());
    }

    @Test
    public void listGamesPass() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        ListGamesResponse res = facade.listGames(new ListGamesRequest(response.getAuthToken()));
        ArrayList<GameData> games = new ArrayList<>();
        games.add(new GameData(1, null, null, "My Game", new ChessGame()));
        games.add(new GameData(2, null, null, "My Game", new ChessGame()));
        assertEquals(2, res.getGames().size());
    }

    @Test
    public void listGamesFail() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        ListGamesResponse res = facade.listGames(new ListGamesRequest(response.getAuthToken()));
        assertEquals(0, res.getGames().size());
    }

    @Test
    public void joinGamePass() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        Response message = facade.joinGame(new JoinRequest(response.getAuthToken(), "Black", "1"));
        Assertions.assertNull(message.getMessage());
    }

    @Test
    public void joinGameFail() throws Exception {
        RegisterResponse response = facade.registerUser(new UserData("player1", "password", "player1@gmail.com"));
        CreateGameResponse gameResponse = facade.createGame(new CreateGameRequest("New Game"), response.getAuthToken());
        Response res = facade.joinGame(new JoinRequest(response.getAuthToken(), "Black", "0"));
        Assertions.assertEquals("Error: bad request", res.getMessage());
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

}
