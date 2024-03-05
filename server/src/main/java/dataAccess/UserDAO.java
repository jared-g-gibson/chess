package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public void createUser(UserData data) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
}
