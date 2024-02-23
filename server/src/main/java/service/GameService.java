package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import request.CreateGameRequest;
import request.GameRequest;

import javax.xml.crypto.Data;

public class GameService {
    public GameService() {

    }
    public String createGame(GameRequest gameRequest) throws DataAccessException {
        AuthDAO auth = new MemoryAuthDAO();
        GameDAO game = new MemoryGameDAO();
        // If authToken does not exist, throw error
        if(gameRequest.authToken() == null || gameRequest.gameName() == null)
            throw new DataAccessException("Error: bad request");
        // If game already exists, throw error
        if(game.getGame(gameRequest.gameName()) != null)
            throw new DataAccessException("Error: bad request");
        // If not authorized, throw error
        if(auth.getAuth(gameRequest.authToken()) == null)
            throw new DataAccessException("Error: unauthorized");

        // Create New Game
        int gameID = game.getNumGames();
        GameData newGame = new GameData(game.getNumGames(), "", "", gameRequest.gameName(), new ChessGame());
        game.createGame(newGame);
        return Integer.toString(gameID);
    }
}
