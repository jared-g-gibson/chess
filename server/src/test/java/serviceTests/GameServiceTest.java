package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.GameRequest;
import request.JoinRequest;
import service.GameService;
import spark.utils.Assert;

import java.util.ArrayList;

class GameServiceTest {

    @Test
    void createGameSuccess() throws Exception {
        // Create new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        // Add Auth Token So that we are Authorized to create game
        String authToken = auths.createAuth("Joe");

        // Create new game request and service
        GameRequest gameRequest = new GameRequest("My Game", authToken);
        GameService service = new GameService(auths, games);

        // Create Game
        service.createGame(gameRequest);
        Assertions.assertEquals(1, games.getNumGames());
        Assertions.assertEquals(new GameData(1, null, null,
            "My Game", games.getGameFromGameName("My Game").game()), games.getGameFromGameName("My Game"));

        // Clear games and auths for next test
        games.clear();
        auths.clear();
    }

    @Test
    void createGameFail() throws DataAccessException {
        // Create new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        // Add Auth Token So that we are Authorized to create game
        String authToken = auths.createAuth("Joe");

        // Create new game request and service without authorization
        GameRequest gameRequest = new GameRequest(null, authToken);
        GameService service = new GameService(auths, games);

        // Create Game and fail because gameName is null
        try{
            service.createGame(gameRequest);
        }
        catch(Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

        // Check that there are 0 games in DAO
        Assertions.assertEquals(0, games.getNumGames());

        // Clear games and auths for next test
        games.clear();
        auths.clear();
    }

    @Test
    void joinGamePass() throws Exception {
        // Create new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        // Add Auth Token So that we are Authorized to create game
        String authToken = auths.createAuth("Joe");

        // Create new game request and service
        GameRequest gameRequest = new GameRequest("My Game", authToken);
        GameService service = new GameService(auths, games);

        // Create Game
        service.createGame(gameRequest);

        // Make a Join Request
        JoinRequest request = new JoinRequest(authToken, "WHITE", "1");

        // Join game
        service.joinGame(request);

        // Assert that created game is equal to expected game
        Assertions.assertEquals(new GameData(1, "Joe", null,
                        "My Game", games.getGameFromGameName("My Game").game()), games.getGameFromGameName("My Game"));

        // Clear games and auths for next test
        games.clear();
        auths.clear();
    }

    @Test
    void joinGameFail() throws Exception {
        // Create new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        // Add Auth Token So that we are Authorized to create game
        String authToken = auths.createAuth("Joe");

        // Create new game request and service
        GameRequest gameRequest = new GameRequest("My Game", authToken);
        GameService service = new GameService(auths, games);

        // Create Game
        service.createGame(gameRequest);

        // Make a Join Request
        JoinRequest request = new JoinRequest(authToken, "WHITE", "1");

        // Joe Join game
        service.joinGame(request);

        // Ruby authToken Creation
        String rubyAuthToken = auths.createAuth("Ruby");

        // Make a Join Request for white spot that is already taken by Joe
        request = new JoinRequest(rubyAuthToken, "WHITE", "1");

        // Try joining white spot that is already taken
        try{
            service.joinGame(request);
        }
        catch(Exception e) {
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }

        // Assert that created game is equal to expected game
        Assertions.assertEquals(new GameData(1, "Joe", null,
                        "My Game", games.getGameFromGameName("My Game").game()), games.getGameFromGameName("My Game"));

        // Clear games and auths for next test
        games.clear();
        auths.clear();
    }

    @Test
    void listGamesPass() throws Exception {
        // Create new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        // Add Auth Token So that we are Authorized to create game
        String authToken = auths.createAuth("Joe");

        // Create new game request and service
        GameRequest gameRequest = new GameRequest("My Game", authToken);
        GameService service = new GameService(auths, games);
        service.createGame(gameRequest);

        // Create another game
        gameRequest = new GameRequest("My Game pt. 2", authToken);
        service = new GameService(auths, games);
        service.createGame(gameRequest);

        // These games should be returned from the listGames method
        ArrayList<GameData> listOfGames = new ArrayList<>();
        listOfGames.add(new GameData(1, null, null, "My Game", games.getGameFromGameName("My Game").game()));
        listOfGames.add(new GameData(2, null, null, "My Game pt. 2", games.getGameFromGameName("My Game pt. 2").game()));

        // Check if listGames works
        Assertions.assertEquals(2, games.getNumGames());
        Assertions.assertEquals(listOfGames, service.listGames(authToken));

        // Clear games and auths for next test
        games.clear();
        auths.clear();

    }

    @Test
    void listGamesFail() throws Exception {
        // Create new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        // Add Auth Token So that we are Authorized to create game
        String authToken = auths.createAuth("Joe");

        // Create new game request and service
        GameRequest gameRequest = new GameRequest("My Game", "I am not authorized");
        GameService service = new GameService(auths, games);
        try {
            service.createGame(gameRequest);
        }
        catch(Exception e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }

        // Create another game
        gameRequest = new GameRequest("My Game pt. 2", "I am not authorized");
        service = new GameService(auths, games);
        try {
            service.createGame(gameRequest);
        }
        catch(Exception e) {
            Assertions.assertEquals( "Error: unauthorized", e.getMessage());
        }

        // Check if listGames works by having 0 games
        Assertions.assertEquals(0, games.getNumGames());
        ArrayList<GameData> listOfGames = new ArrayList<>();
        Assertions.assertEquals(listOfGames, service.listGames(authToken));

        // Clear games and auths for next test
        games.clear();
        auths.clear();
    }
}