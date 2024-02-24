package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    @Test
    void clear() throws Exception{
        // Clear test creates new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();
        UserDAO users = new MemoryUserDao();
        // Adding auth, game and user to auths, games, and users
        auths.createAuth("Joe");
        games.createGame(new GameData(0, "White", "Black", "Game", new ChessGame()));
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        // Assert that they were added to DAOs successfully
        Assertions.assertNotEquals(auths.getNumAuths(), 0);
        Assertions.assertNotEquals(games.getNumGames(), 0);
        Assertions.assertNotEquals(users.getNumUsers(), 0);
        // Call the clear function
        ClearService service = new ClearService(auths, games, users);
        service.clear();
        // Assert that clear function successfully cleared DAOs
        Assertions.assertEquals(auths.getNumAuths(), 0);
        Assertions.assertEquals(games.getNumGames(), 0);
        Assertions.assertEquals(users.getNumUsers(), 0);
    }
}