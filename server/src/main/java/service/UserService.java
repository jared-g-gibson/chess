package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;

public class UserService {
    public String register(UserData userData) throws DataAccessException {
        UserDAO user = new MemoryUserDao();
        // Check if the user is taken
        if(null != user.getUser(userData.username()))
            throw new DataAccessException("Error: already taken");

        // Create User
        user.createUser(userData);

        // Create Auth Token from User
        AuthDAO auth = new MemoryAuthDAO();
        String authToken = auth.createAuth(userData.username());

        // Return Auth Token
        return authToken;
    }
    public AuthData login(UserData user) {
        return null;
    }
    public void logout(UserData user) {}
}
