package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void createGame(GameData gameData);
    public GameData getGame(String gameName);
    public Collection<GameData> getGames();
    public void updateGame();
    public void clear();
    public int getNumGames();
}
