package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

class ClearServiceTest {

    @Test
    void clear() throws Exception{
        // Clear test creates new Data Access Objects
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();
        UserDAO users = new MemoryUserDAO();
        // Adding auth, game and user to auths, games, and users
        String authToken = auths.createAuth("Joe");
        games.createGame(new GameData(1, "White", "Black", "Game", new ChessGame()));
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        // Assert that they were added to DAOs successfully
        Assertions.assertNotNull(auths.getAuth(authToken));
        Assertions.assertNotNull(games.getGame("1"));
        Assertions.assertNotNull(users.getUser("Joe"));
        // Call the clear function
        ClearService service = new ClearService(auths, games, users);
        service.clear();
        // Assert that clear function successfully cleared DAOs
        Assertions.assertNull(auths.getAuth(authToken));
        Assertions.assertNull(games.getGame("1"));
        Assertions.assertNull(users.getUser("Joe"));
    }
}