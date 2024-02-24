package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import dataAccess.GameDAO;
import request.JoinGameRequest;
import request.JoinRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{

    private AuthDAO auths;
    private GameDAO games;
    public JoinGameHandler(AuthDAO auths, GameDAO games) {
        this.auths = auths;
        this.games = games;
    }
    @Override
    public String handle(Request req, Response res) {
        var serializer = new Gson();
        JoinGameRequest joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
        JoinRequest joinRequest = new JoinRequest(req.headers("authorization"), joinGameRequest.playerColor(), joinGameRequest.gameID());
        GameService service = new GameService(auths, games);
        try {
            service.joinGame(joinRequest);
        } catch (DataAccessException e) {
            return e.getException(res);
        }
        res.status(200);
        return "{}";
    }
}
