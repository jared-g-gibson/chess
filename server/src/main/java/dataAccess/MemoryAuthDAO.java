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
        String authToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(authToken, username);
        auths.put(authToken, data);
        return data.authToken();
    }
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }
    public int getNumAuths() {
        return auths.size();
    }
}
