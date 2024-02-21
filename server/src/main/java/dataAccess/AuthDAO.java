package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth();
    public AuthData getAuth();
    public void deleteAuth();
}
