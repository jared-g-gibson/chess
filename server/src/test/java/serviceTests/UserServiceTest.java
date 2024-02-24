package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import service.UserService;

class UserServiceTest {

    @Test
    void registerPass() throws Exception {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        // Create a new user
        UserData joe = new UserData("Joe", "password", "joe@gmail.com");

        // Use Register Service to add Joe
        UserService service = new UserService(auths, users);
        service.register(joe);

        // Check if added successfully
        Assertions.assertNotNull(users.getUser("Joe"));
        Assertions.assertEquals(new UserData("Joe", "password", "joe@gmail.com"), users.getUser("Joe"));

        // Clear users and auths for future tests
        users.clear();
        auths.clear();
    }

    @Test
    void registerFail() throws Exception{
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        // Create a new user
        UserData joe = new UserData("Joe", "password", "joe@gmail.com");

        // Use Register Service to add Joe
        UserService service = new UserService(auths, users);
        service.register(joe);

        // Try registering Joe again, it should throw an error
        try {
            service.register(joe);
        }
        catch(Exception e) {
            Assertions.assertEquals( "Error: already taken", e.getMessage());
        }

        Assertions.assertNotNull(users.getUser("Joe"));
        Assertions.assertEquals(new UserData("Joe", "password", "joe@gmail.com"), users.getUser("Joe"));

        // Clear users and auths for future tests
        users.clear();
        auths.clear();
    }

    @Test
    void loginPass() throws Exception {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        // Create a new user
        UserData joe = new UserData("Joe", "password", "joe@gmail.com");

        // Use Register Service to add Joe
        UserService service = new UserService(auths, users);
        service.register(joe);

        // Create Login Request
        LoginRequest loginRequest = new LoginRequest("Joe", "password");

        // Try to login
        String authToken = service.login(loginRequest);

        // Check logged in successfully
        Assertions.assertNotNull(users.getUser("Joe"));
        Assertions.assertEquals(new AuthData(authToken, "Joe"), auths.getAuth(authToken));

        // Clear users and auths for future tests
        users.clear();
        auths.clear();
    }

    @Test
    void loginFail() throws Exception {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        // Create a new user
        UserData joe = new UserData("Joe", "password", "joe@gmail.com");

        // Use Register Service to add Joe
        UserService service = new UserService(auths, users);
        service.register(joe);

        // Create Login Request
        LoginRequest loginRequest = new LoginRequest("Joe", "incorrectPassword");

        // Try to login
        String authToken = null;

        // See if error is correct
        try {
            authToken = service.login(loginRequest);
        }
        catch(Exception e) {
            Assertions.assertEquals( "Error: unauthorized", e.getMessage());
        }

        // Check if authToken is 0
        Assertions.assertEquals(null, auths.getAuth(authToken));

        // Clear users and auths for future tests
        users.clear();
        auths.clear();
    }

    @Test
    void logoutPass() throws DataAccessException {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        // Create a new user
        UserData joe = new UserData("Joe", "password", "joe@gmail.com");

        // Use Register Service to add Joe
        UserService service = new UserService(auths, users);
        service.register(joe);

        // Create Login Request
        LoginRequest loginRequest = new LoginRequest("Joe", "password");

        // Try to login
        String authToken = service.login(loginRequest);

        // Check logged in successfully
        Assertions.assertNotNull(users.getUser("Joe"));
        Assertions.assertEquals(new AuthData(authToken, "Joe"), auths.getAuth(authToken));

        // Try to logout
        service.logout(authToken);

        // Checked logged out successfully
        Assertions.assertEquals(null, auths.getAuth(authToken));

        // Clear users and auths for future tests
        users.clear();
        auths.clear();
    }

    @Test
    void logoutFail() throws DataAccessException {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        // Create a new user
        UserData joe = new UserData("Joe", "password", "joe@gmail.com");

        // Use Register Service to add Joe
        UserService service = new UserService(auths, users);
        service.register(joe);

        // Create Login Request
        LoginRequest loginRequest = new LoginRequest("Joe", "password");

        // Try to login
        String authToken = service.login(loginRequest);

        // Check logged in successfully
        Assertions.assertNotNull(users.getUser("Joe"));
        Assertions.assertEquals(new AuthData(authToken, "Joe"), auths.getAuth(authToken));

        // Try to logout
        try {
            service.logout("Unauthorized authToken");
        }
        catch(Exception e) {
            Assertions.assertEquals( "Error: unauthorized", e.getMessage());
        }

        // Checked logged out successfully
        Assertions.assertEquals(new AuthData(authToken, "Joe"), auths.getAuth(authToken));

        // Clear users and auths for future tests
        users.clear();
        auths.clear();
    }
}