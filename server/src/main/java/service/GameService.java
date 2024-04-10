package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import request.GameRequest;
import request.JoinRequest;

import java.util.ArrayList;

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
        // if(games.getGameFromGameName(gameRequest.gameName()) != null)
            // throw new DataAccessException("Error: bad request");
        // If not authorized, throw error
        if(auths.getAuth(gameRequest.authToken()) == null)
            throw new DataAccessException("Error: unauthorized");

        // Create New Game
        int gameID = games.getNumGames() + 1;
        GameData newGame = new GameData(gameID, null, null, gameRequest.gameName(), new ChessGame());
        games.createGame(newGame);
        return Integer.toString(gameID);
    }

    public void joinGame(JoinRequest joinRequest) throws DataAccessException {
        // If not authorized, throw error
        if(auths.getAuth(joinRequest.authToken()) == null)
            throw new DataAccessException("Error: unauthorized");
        // Game does not exist
        if(games.getGame(joinRequest.gameID()) == null)
            throw new DataAccessException("Error: bad request");
        // If playerColor is empty, return. They are an observer
        if(joinRequest.color() == null)
            return;
        // White is already taken
        if(joinRequest.color().equals("WHITE") && games.getGame(joinRequest.gameID()).whiteUsername() != null)
            throw new DataAccessException("Error: already taken");
        // Black is already taken
        if(joinRequest.color().equals("BLACK") && games.getGame(joinRequest.gameID()).blackUsername() != null)
            throw new DataAccessException("Error: already taken");

        // Update games based on playerColor
        games.updateGame(joinRequest.color(), auths.getAuth(joinRequest.authToken()).username(), joinRequest.gameID());

    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        // If not authorized, throw error
        if(auths.getAuth(authToken) == null)
            throw new DataAccessException("Error: unauthorized");
        // Return games
        return games.getGames();
    }

    public void joinPlayer() {

    }
}
