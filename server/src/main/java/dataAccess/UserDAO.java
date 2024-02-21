package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear();
    public void createUser();
    public UserData getUser();

}
