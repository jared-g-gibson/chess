package dataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<String, GameData> games = new HashMap<>();

    public void createGame(GameData gameData) {
        games.put(gameData.gameName(), gameData);
    }
    public GameData getGame(String gameName) {
        return games.get(gameName);
    }
    public Collection<GameData> getGames() {
        return null;
    }

    public int getNumGames() {
        return games.size();
    }
    public void updateGame() {

    }
    public void clear() {
        games.clear();
    }
}
