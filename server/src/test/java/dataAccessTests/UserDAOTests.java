package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.LoginRequest;

public class UserDAOTests {

    @Test
    public void clearUsers() throws DataAccessException {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertNotNull(users.getUser("Joe"));
        users.clear();
        Assertions.assertNull(users.getUser("Joe"));

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertNotNull(memoryUsers.getUser("Joe"));
        memoryUsers.clear();
        Assertions.assertNull(memoryUsers.getUser("Joe"));
    }

    @Test
    public void createUserPass() throws DataAccessException {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertNotNull(users.getUser("Joe"));

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertNotNull(memoryUsers.getUser("Joe"));

        // Clear for next test
        users.clear();
        memoryUsers.clear();
    }

    @Test
    public void createUserFail() throws DataAccessException {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        try {
            users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            Assertions.fail();
        }
        catch(Exception e) {
            Assertions.assertEquals( "Error: already taken", e.getMessage());
        }

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        try {
            memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            Assertions.fail();
        }
        catch(Exception e) {
            Assertions.assertEquals( "Error: already taken", e.getMessage());
        }

        // Clear for next test
        users.clear();
        memoryUsers.clear();
    }

    @Test
    public void getUserPass() throws DataAccessException {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();

        // Password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        // Check username, password, and email
        Assertions.assertTrue(encoder.matches("password", users.getUser("Joe").password()));
        Assertions.assertEquals("Joe", users.getUser("Joe").username());
        Assertions.assertEquals("joe@gmail.com", users.getUser("Joe").email());

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertEquals(new UserData("Joe", "password", "joe@gmail.com"), memoryUsers.getUser("Joe"));

        // Clear for next test
        users.clear();
        memoryUsers.clear();
    }

    @Test
    public void getUserFail() throws DataAccessException{
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();

        // Password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        // Checks if no user is retrieved if username does not exist
        Assertions.assertNull(users.getUser("J"));

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertNull(memoryUsers.getUser("J"));

        // Clear for next test
        users.clear();
        memoryUsers.clear();
    }

    @Test
    public void verifyUserPass() throws DataAccessException{
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertTrue(users.verifyUser(new LoginRequest("Joe", "password")));

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertTrue(memoryUsers.verifyUser(new LoginRequest("Joe", "password")));

        // Clear for next test
        users.clear();
        memoryUsers.clear();
    }

    @Test
    public void verifyUserFail() throws DataAccessException{
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        // SQL
        users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertTrue(users.verifyUser(new LoginRequest("Joe", "password")));
        try {
            users.verifyUser(new LoginRequest("Joe", "incorrectPassword"));
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }

        // Memory
        memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
        Assertions.assertTrue(memoryUsers.verifyUser(new LoginRequest("Joe", "password")));
        try {
            users.verifyUser(new LoginRequest("Joe", "incorrectPassword"));
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }

        // Clear for next test
        users.clear();
        memoryUsers.clear();
    }


}
