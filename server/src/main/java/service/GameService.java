package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import request.CreateGameRequest;
import request.GameRequest;

import javax.xml.crypto.Data;

public class GameService {
    private AuthDAO auths;
    private GameDAO games;
    public GameService(AuthDAO auths, GameDAO games) {
        this.auths = auths;
        this.games = games;
    }
    public String createGame(GameRequest gameRequest) throws DataAccessException {
        // If authToken does not exist, throw error
        if(gameRequest.authToken() == null || gameRequest.gameName() == null)
            throw new DataAccessException("Error: bad request");
        // If game already exists, throw error
        if(games.getGame(gameRequest.gameName()) != null)
            throw new DataAccessException("Error: bad request");
        // If not authorized, throw error
        if(auths.getAuth(gameRequest.authToken()) == null)
            throw new DataAccessException("Error: unauthorized");

        // Create New Game
        int gameID = games.getNumGames() + 1;
        GameData newGame = new GameData(games.getNumGames(), "", "", gameRequest.gameName(), new ChessGame());
        games.createGame(newGame);
        return Integer.toString(gameID);
    }
}
