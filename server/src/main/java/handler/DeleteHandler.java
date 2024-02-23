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
            res.status(500);
            ErrorMessage message = new ErrorMessage("Error: " + e.getMessage());
            return serializer.toJson(message);
        }
        res.status(200);
        return "{}";
    }
}
