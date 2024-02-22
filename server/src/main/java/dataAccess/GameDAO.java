package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void createGame();
    public GameData getGame();
    public Collection<GameData> getGames();
    public void updateGame();
    public void clear();
}
