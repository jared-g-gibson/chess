package dataAccess;

import model.UserData;

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
}
