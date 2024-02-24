package dataAccess;

import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    public void createGame(GameData gameData);
    public GameData getGame(String gameID);
    public ArrayList<GameData> getGames();
    public GameData getGameFromGameName(String gameName);
    public void updateGame(String color, String username, String gameID);
    public void clear();
    public int getNumGames();
}
