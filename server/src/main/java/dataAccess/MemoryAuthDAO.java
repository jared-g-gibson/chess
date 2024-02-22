package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<String, AuthData> auths = new HashMap<>();

    public MemoryAuthDAO() {
    }
    public void clear() {
        auths.clear();
    }
    public String createAuth(String username) {
        AuthData data = new AuthData(UUID.randomUUID().toString(), username);
        auths.put(username, data);
        return data.authToken();
    }
    public AuthData getAuth() {
        return null;
    }
    public void deleteAuth() {

    }
}
