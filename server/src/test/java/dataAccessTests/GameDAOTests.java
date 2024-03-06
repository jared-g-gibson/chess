package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameDAOTests {
    @Test
    public void clearGames() {
        GameDAO games = new SQLGameDAO();
        GameDAO memoryGames = new MemoryGameDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
