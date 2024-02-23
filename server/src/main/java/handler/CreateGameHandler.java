package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import request.CreateGameRequest;
import request.GameRequest;
import request.LoginRequest;
import response.CreateGameResponse;
import response.LoginResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {

    @Override
    public String handle(Request req, Response res) {
        var serializer = new Gson();
        CreateGameRequest gameName = serializer.fromJson(req.body(), CreateGameRequest.class);
        GameRequest gameRequest = new GameRequest(gameName.gameName(), req.headers("Authorization"));
        GameService service = new GameService();
        String gameID = null;
        try {
            gameID = service.createGame(gameRequest);
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
            else {
                res.status(500);
                ErrorMessage message = new ErrorMessage("Error: " + e.getMessage());
                return serializer.toJson(message);
            }
        }
        res.status(200);
        CreateGameResponse response = new CreateGameResponse(null, gameID);
        return serializer.toJson(response);
    }
}
