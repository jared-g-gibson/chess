package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public String createAuth(String username);
    public AuthData getAuth();
    public void deleteAuth();
    public void clear();
}
