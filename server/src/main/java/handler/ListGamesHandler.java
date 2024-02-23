package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import dataAccess.GameDAO;
import model.GameData;
import request.ListGamesRequest;
import response.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGamesHandler extends Handler{

    private AuthDAO auths;
    private GameDAO games;
    public ListGamesHandler(AuthDAO auths, GameDAO games) {
        this.auths = auths;
        this.games = games;
    }

    @Override
    public String handle(Request req, Response res) {
        var serializer = new Gson();
        // ListGamesRequest request = serializer.fromJson(req.headers("authorization"), ListGamesRequest.class);
        GameService service = new GameService(auths, games);
        ArrayList<GameData> games = null;
        try {
            games = service.listGames(req.headers("authorization"));
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
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
        ListGamesResponse response = new ListGamesResponse(null, games);
        return serializer.toJson(response);
    }
}
