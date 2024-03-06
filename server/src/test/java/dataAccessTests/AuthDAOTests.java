package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {
    @Test
    public void clearAuths() {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();


        try {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createAuthPass() {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // TODO: Add same authToken twice
    @Test
    public void createAuthFail() {
        try {

        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void getAuthPass() {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void getAuthFail() {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void deleteAuthPass() {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        try {
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
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // TODO:
    @Test
    public void deleteAuthFail() {
        AuthDAO auths = new SQLAuthDAO();
        AuthDAO memoryAuths = new MemoryAuthDAO();
        try {
            // SQL
            String authToken = auths.createAuth("Joe");
            Assertions.assertNotNull(auths.getAuth(authToken));
            auths.deleteAuth("Wrong authToken");
            Assertions.assertNull(auths.getAuth(authToken));

            // Memory
            authToken = memoryAuths.createAuth("Joe");
            Assertions.assertNotNull(memoryAuths.getAuth(authToken));
            memoryAuths.deleteAuth("Wrong authToken");
            Assertions.assertNull(auths.getAuth(authToken));

            // Clear for next test
            auths.clear();
            memoryAuths.clear();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
