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
        JoinRequest joinRequest = new JoinRequest(req.headers("authorization"), joinGameRequest.color(), joinGameRequest.gameID());
        GameService service = new GameService(auths, games);
        try {
            service.joinGame(joinRequest);
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
                ErrorMessage message = new ErrorMessage(e.getMessage());
                return serializer.toJson(message);
            }
            else if(e.getMessage().equals("Error: bad request")) {
                res.status(400);
                ErrorMessage message = new ErrorMessage(e.getMessage());
                return serializer.toJson(message);
            }
            else if(e.getMessage().equals("Error: already taken")) {
                res.status(403);
                ErrorMessage message = new ErrorMessage(e.getMessage());
                return serializer.toJson(message);
            }
            else {
                res.status(500);
                ErrorMessage message = new ErrorMessage("Error: " + e.getMessage());
                return serializer.toJson(message);
            }
        }
        res.status(200);
        return "{}";
    }
}
