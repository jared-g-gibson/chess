package dataAccess;

import model.UserData;
import request.LoginRequest;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private static final HashMap<String, UserData> users = new HashMap<>();

    public void clear() {
        users.clear();
    }
    public void createUser(UserData data) {
        users.put(data.username(), data);
    }
    public UserData getUser(String username) {
        return users.get(username);
    }

    public void verifyUser(LoginRequest loginInfo) throws DataAccessException {
        // If username does not exist, throw error
        if(getUser(loginInfo.username()) == null)
            throw new DataAccessException("Error: unauthorized");

        // If usernames and passwords do not match, throw error
        if(!getUser(loginInfo.username()).username().equals(loginInfo.username()) ||
                !getUser(loginInfo.username()).password().equals(loginInfo.password()))
            throw new DataAccessException("Error: unauthorized");
    }
}
