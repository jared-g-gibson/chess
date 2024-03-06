package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import model.UserData;
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
}
