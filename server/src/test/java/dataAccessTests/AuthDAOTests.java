package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {
    @Test
    public void clearAuths() throws DataAccessException {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        // SQL
        String authToken = auths.createAuth("Joe");
        Assertions.assertNotNull(auths.getAuth(authToken));
        auths.clear();
        Assertions.assertNull(auths.getAuth("Joe"));

        // Memory
        authToken = memoryAuths.createAuth("Joe");
        Assertions.assertNotNull(memoryAuths.getAuth(authToken));
        memoryAuths.clear();
        Assertions.assertNull(memoryAuths.getAuth("Joe"));
    }

    @Test
    public void createAuthPass() throws DataAccessException {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        // SQL
        String authToken = auths.createAuth("Joe");
        Assertions.assertNotNull(auths.getAuth(authToken));

        // Memory
        authToken = memoryAuths.createAuth("Joe");
        Assertions.assertNotNull(memoryAuths.getAuth(authToken));

        // Clear for next test
        auths.clear();
        memoryAuths.clear();
    }

    @Test
    public void createAuthFail() throws DataAccessException {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();

        // SQL
        String authToken = auths.createAuth("Joe");
        Assertions.assertNotNull(auths.getAuth(authToken));
        try {
            authToken = auths.createAuth("Joe;");
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: invalid username", e.getMessage());
        }

        // Clear for next test
        auths.clear();
        memoryAuths.clear();
    }

    @Test
    public void getAuthPass() throws DataAccessException {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        // SQL
        String authToken = auths.createAuth("Jake");
        Assertions.assertNotNull(auths.getAuth(authToken));

        // Memory
        authToken = memoryAuths.createAuth("Jake");
        Assertions.assertNotNull(memoryAuths.getAuth(authToken));

        // Clear for next test
        auths.clear();
        memoryAuths.clear();
    }

    @Test
    public void getAuthFail() throws DataAccessException{
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        // SQL
        String authToken = auths.createAuth("Joe");
        Assertions.assertNull(auths.getAuth("Wrong authToken"));

        // Memory
        authToken = memoryAuths.createAuth("Jake");
        Assertions.assertNull(memoryAuths.getAuth("Wrong authToken"));

        // Clear for next test
        auths.clear();
        memoryAuths.clear();
    }

    @Test
    public void deleteAuthPass() throws DataAccessException {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        // SQL
        String authToken = auths.createAuth("Joe");
        Assertions.assertNotNull(auths.getAuth(authToken));
        auths.deleteAuth(authToken);
        Assertions.assertNull(auths.getAuth(authToken));

        // Memory
        authToken = memoryAuths.createAuth("Joe");
        Assertions.assertNotNull(memoryAuths.getAuth(authToken));
        memoryAuths.deleteAuth(authToken);
        Assertions.assertNull(auths.getAuth(authToken));

        // Clear for next test
        auths.clear();
        memoryAuths.clear();
    }

    @Test
    public void deleteAuthFail() throws DataAccessException {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        // SQL
        String authToken = auths.createAuth("Joe");
        Assertions.assertNotNull(auths.getAuth(authToken));
        try {
            auths.deleteAuth("Wrong authToken");
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

        // Memory
        authToken = memoryAuths.createAuth("Joe");
        Assertions.assertNotNull(memoryAuths.getAuth(authToken));
        try {
            memoryAuths.deleteAuth("Wrong authToken");
            Assertions.fail();
        }
        catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

        // Clear for next test
        auths.clear();
        memoryAuths.clear();
    }
}
