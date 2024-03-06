package dataAccessTests;

import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDAOTests {

    @Test
    public void clearUsers() {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createUserPass() {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createUserFail() {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();
        try {
            // SQL
            users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            try {
                users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            }
            catch(Exception e) {
                Assertions.assertEquals( "Error: already taken", e.getMessage());
            }

            // Memory
            memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            try {
                memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            }
            catch(Exception e) {
                Assertions.assertEquals( "Error: already taken", e.getMessage());
            }

            // Clear for next test
            users.clear();
            memoryUsers.clear();

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserPass() {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();

        // Password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserFail() {
        UserDAO users = new SQLUserDAO();
        UserDAO memoryUsers = new MemoryUserDAO();

        // Password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            // SQL
            users.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            // Checks if no user is retreived if username does not exist
            Assertions.assertNull(users.getUser("J"));

            // Memory
            memoryUsers.createUser(new UserData("Joe", "password", "joe@gmail.com"));
            Assertions.assertNull(memoryUsers.getUser("J"));

            // Clear for next test
            users.clear();
            memoryUsers.clear();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
