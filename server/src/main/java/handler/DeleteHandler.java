package handler;

import com.google.gson.Gson;
import dataAccess.*;
import service.ClearService;
import spark.Request;
import spark.Response;

public class DeleteHandler extends Handler {

    private AuthDAO auths;
    private GameDAO games;
    private UserDAO users;

    public DeleteHandler(AuthDAO auths, GameDAO games, UserDAO users) {
        this.auths = auths;
        this.games = games;
        this.users = users;
    }
    public String handle(Request req, Response res) {
        ClearService clear = new ClearService(auths, games, users);
        var serializer = new Gson();
        try {
            clear.clear();
        } catch (DataAccessException e) {
            return e.getException(res);
        }
        res.status(200);
        return "{}";
    }
}
