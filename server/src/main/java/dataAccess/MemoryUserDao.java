package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDAO {

    private static final HashMap<String, UserData> users = new HashMap<>();

    public void clear() {
        users.clear();
    }
    public void createUser(UserData data) {
        users.put(data.username(), data);
        return;
    };
    public UserData getUser(String username) {
        return users.get(username);
    }
    public int getNumUsers() {
        return users.size();
    }
}
