package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;

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
    public String login(LoginRequest loginInfo) throws DataAccessException {
        UserDAO user = new MemoryUserDao();
        // If username does not exist, throw error
        if(user.getUser(loginInfo.username()) == null)
            throw new DataAccessException("Error: unauthorized");

        // If usernames and passwords do not match, throw error
        if(!user.getUser(loginInfo.username()).username().equals(loginInfo.username()) ||
            !user.getUser(loginInfo.username()).password().equals(loginInfo.password()))
            throw new DataAccessException("Error: unauthorized");

        // Create Auth Token from User
        AuthDAO auth = new MemoryAuthDAO();
        String authToken = auth.createAuth(loginInfo.username());

        return authToken;
    }
    public void logout(UserData user) {}
}
