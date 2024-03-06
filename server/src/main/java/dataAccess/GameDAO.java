package dataAccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public interface GameDAO {
    public void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(String gameID) throws DataAccessException;
    public ArrayList<GameData> getGames() throws DataAccessException;
    public GameData getGameFromGameName(String gameName);
    public void updateGame(String color, String username, String gameID);
    public void clear();
    public int getNumGames();
}
