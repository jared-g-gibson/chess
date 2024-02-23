package service;

import dataAccess.*;
import model.UserData;

public class ClearService {

    private AuthDAO auths;
    private GameDAO games;
    private UserDAO users;

    public ClearService(AuthDAO auths, GameDAO games, UserDAO users) {
        this.auths = auths;
        this.games = games;
        this.users = users;
    }

    public void clear() throws DataAccessException {
        this.auths.clear();
        this.games.clear();
        this.users.clear();
    }
}
