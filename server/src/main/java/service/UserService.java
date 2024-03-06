package service;

import dataAccess.*;
import model.UserData;
import request.LoginRequest;

public class UserService {
    private AuthDAO auths;
    private UserDAO users;
    public UserService(AuthDAO auths, UserDAO users) {
        this.auths = auths;
        this.users = users;
    }
    public String register(UserData userData) throws DataAccessException {
        // Check if the user is taken
        if(null != users.getUser(userData.username()))
            throw new DataAccessException("Error: already taken");

        // Create User
        users.createUser(userData);

        // Create Auth Token from User
        String authToken = auths.createAuth(userData.username());

        // Return Auth Token
        return authToken;
    }
    public String login(LoginRequest loginInfo) throws DataAccessException {

        // Verify if User has correct credentials to login
        users.verifyUser(loginInfo);

        // Create Auth Token from User
        return auths.createAuth(loginInfo.username());
    }
    public void logout(String authToken) throws DataAccessException {

        // If authToken does not exist, throw error
        if(auths.getAuth(authToken) == null)
            throw new DataAccessException("Error: unauthorized");

        // Remove authToken
        auths.deleteAuth(authToken);
    }
}
