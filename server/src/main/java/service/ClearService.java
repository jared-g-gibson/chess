package service;

import dataAccess.*;
import model.UserData;

public class ClearService {
    public void clear() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDao();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}
