package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDAOTests {
    @Test
    public void clearGames() throws DataAccessException{
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        games.clear();
        memoryGames.clear();

        // SQL
        games.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNotNull(games.getGame("1"));
        games.clear();
        Assertions.assertEquals(0, games.getNumGames());
        Assertions.assertNull(games.getGame("1"));

        // Memory
        memoryGames.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNotNull(memoryGames.getGame("1"));
        memoryGames.clear();
        Assertions.assertEquals(0, memoryGames.getNumGames());
        Assertions.assertNull(memoryGames.getGame("1"));
    }

    @Test
    public void createGamePass() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNotNull(games.getGame("1"));

        // Memory
        memoryGames.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNotNull(memoryGames.getGame("1"));

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void createGameFail() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();

        // SQL
        games.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        try {
            games.createGame(new GameData(2, "Jack", "Hazel", "My Game", new ChessGame()));
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }
        Assertions.assertEquals(1, games.getNumGames());

        // Memory
        memoryGames.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        try {
            memoryGames.createGame(new GameData(2, "Jack", "Hazel", "My Game", new ChessGame()));
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }
        Assertions.assertEquals(1, memoryGames.getNumGames());

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getGamePass() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNotNull(games.getGame("1"));

        // Memory
        memoryGames.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNotNull(memoryGames.getGame("1"));

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getGameFail() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNull(games.getGame("0"));

        // Memory
        memoryGames.createGame(new GameData(1, "Joe", "Ruby", "My Game", new ChessGame()));
        Assertions.assertNull(memoryGames.getGame("0"));

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getGamesPass() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // Memory
        memoryGames.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        memoryGames.createGame(new GameData(2, null, null, "My Game pt. 2", new ChessGame()));
        // These games should be returned from the listGames method
        ArrayList<GameData> memoryListOfGames = new ArrayList<>();
        memoryListOfGames.add(new GameData(1, null, null, "My Game", memoryGames.getGame("1").game()));
        memoryListOfGames.add(new GameData(2, null, null, "My Game pt. 2", memoryGames.getGame("2").game()));
        // Check if ArrayList is equal to SQL list of Games
        Assertions.assertEquals(memoryListOfGames, memoryGames.getGames());

        // SQL
        games.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        games.createGame(new GameData(2, null, null, "My Game pt. 2", new ChessGame()));
        // These games should be returned from the listGames method
        ArrayList<GameData> listOfGames = new ArrayList<>();
        listOfGames.add(new GameData(1, null, null, "My Game", games.getGame("1").game()));
        listOfGames.add(new GameData(2, null, null, "My Game pt. 2", games.getGame("2").game()));

        // Check if ArrayList is equal to SQL list of Games
        ArrayList<GameData> sqlListOfGames = games.getGames();

        // Check if Game Ids are the same
        Assertions.assertEquals(listOfGames.getFirst().gameID(), sqlListOfGames.getFirst().gameID());
        Assertions.assertEquals(listOfGames.get(1).gameID(), sqlListOfGames.get(1).gameID());

        // Check if Game names are equal
        Assertions.assertEquals(listOfGames.getFirst().gameName(), sqlListOfGames.getFirst().gameName());
        Assertions.assertEquals(listOfGames.get(1).gameName(), sqlListOfGames.get(1).gameName());

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getGamesFail() throws DataAccessException{
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        ArrayList<GameData> sqlListOfGames = games.getGames();
        Assertions.assertEquals(0, sqlListOfGames.size());

        // Memory
        ArrayList<GameData> memoryListOfGames = games.getGames();
        Assertions.assertEquals(0, memoryListOfGames.size());

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getGameFromGameNamePass() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        Assertions.assertNotNull(games.getGameFromGameName("My Game"));

        // Memory
        memoryGames.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        Assertions.assertNotNull(memoryGames.getGameFromGameName("My Game"));

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getGameFromGameNameFail() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        Assertions.assertNull(games.getGameFromGameName("Non Existent Game"));

        // Memory
        memoryGames.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        Assertions.assertNull(memoryGames.getGameFromGameName("Non Existent Game"));

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void updateGamePass() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        games.updateGame("BLACK", "Joe", "1");
        Assertions.assertEquals("Joe", games.getGameFromGameName("My Game").blackUsername());

        // Memory
        memoryGames.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        memoryGames.updateGame("BLACK", "Joe", "1");
        Assertions.assertEquals("Joe", memoryGames.getGameFromGameName("My Game").blackUsername());

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void updateGameFail() throws DataAccessException{
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        games.updateGame("BLACK", "Joe", "1");
        Assertions.assertEquals("Joe", games.getGame("1").blackUsername());
        try {
            games.updateGame("BLACK", "Jack", "1");
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }


        // Memory
        memoryGames.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        memoryGames.updateGame("BLACK", "Joe", "1");
        Assertions.assertEquals("Joe", memoryGames.getGameFromGameName("My Game").blackUsername());
        try {
            memoryGames.updateGame("BLACK", "Jack", "1");
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }

        // Clear for next test
        games.clear();
        memoryGames.clear();
    }

    @Test
    public void getNumGamesPass() throws DataAccessException {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        games.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        Assertions.assertEquals(1, games.getNumGames());

        // Memory
        memoryGames.createGame(new GameData(1, null, null, "My Game", new ChessGame()));
        Assertions.assertEquals(1, memoryGames.getNumGames());

        // Clear for next test
        games.clear();
        memoryGames.clear();

    }

    @Test
    public void getNumGamesFail() throws DataAccessException{
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        // SQL
        Assertions.assertEquals(0, games.getNumGames());

        // Memory
        Assertions.assertEquals(0, memoryGames.getNumGames());

        // Clear for next test
        games.clear();
        memoryGames.clear();

    }

}
