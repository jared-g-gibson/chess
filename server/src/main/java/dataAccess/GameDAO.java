package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public interface GameDAO {
    public void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(String gameID) throws DataAccessException;
    public ArrayList<GameData> getGames() throws DataAccessException;
    public GameData getGameFromGameName(String gameName) throws DataAccessException;
    public void updateGame(String color, String username, String gameID) throws DataAccessException;
    public void clear();
    public int getNumGames();

    public void removePlayer(String gameID, ChessGame.TeamColor teamColor) throws DataAccessException;

    public void updateGameState(String gameID, ChessGame updatedGame) throws DataAccessException;
}
