package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import request.CreateGameRequest;
import request.GameRequest;
import response.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
    private AuthDAO auths;
    private GameDAO games;
    public CreateGameHandler(AuthDAO auths, GameDAO games) {
        this.auths = auths;
        this.games = games;
    }

    @Override
    public String handle(Request req, Response res) {
        var serializer = new Gson();
        CreateGameRequest gameName = serializer.fromJson(req.body(), CreateGameRequest.class);
        GameRequest gameRequest = new GameRequest(gameName.gameName(), req.headers("Authorization"));
        GameService service = new GameService(auths, games);
        String gameID = null;
        try {
            gameID = service.createGame(gameRequest);
        } catch (DataAccessException e) {
            return e.getException(res);
        }
        res.status(200);
        CreateGameResponse response = new CreateGameResponse(null, gameID);
        return serializer.toJson(response);
    }
}
