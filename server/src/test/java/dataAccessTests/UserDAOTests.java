package dataAccessTests;

import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
