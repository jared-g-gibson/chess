package dataAccess;

import model.UserData;
import request.LoginRequest;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public void createUser(UserData data) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public boolean verifyUser(LoginRequest loginInfo) throws DataAccessException;
}
