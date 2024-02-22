package dataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<String, UserData> games = new HashMap<>();

    public void createGame() {

    }
    public GameData getGame() {
        return null;
    }
    public Collection<GameData> getGames() {
        return null;
    }
    public void updateGame() {

    }
    public void clear() {
        games.clear();
    }
}
