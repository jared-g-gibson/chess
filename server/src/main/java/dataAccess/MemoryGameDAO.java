package dataAccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<String, GameData> games = new HashMap<>();

    public void createGame(GameData gameData) {
        games.put(Integer.toString(gameData.gameID()), gameData);
    }
    public GameData getGame(String gameID) {
        return games.get(gameID);
    }

    public GameData getGameFromGameName(String gameName) {
        for(HashMap.Entry<String, GameData> entry : games.entrySet()) {
            GameData value = entry.getValue();
            if(gameName.equals(value.gameName()))
                return value;
        }
        return null;
    }

    public ArrayList<GameData> getGames() {
        ArrayList<GameData> gameList = new ArrayList<>();
        for(HashMap.Entry<String, GameData> entry : games.entrySet()) {
            GameData value = entry.getValue();
            gameList.add(value);
        }
        return gameList;
    }

    public int getNumGames() {
        return games.size();
    }
    public void updateGame(String color, String username, String gameID) {
        if(color.equals("WHITE")) {
            games.put(gameID, games.get(gameID).addWhiteUser(username));
        }
        else if(color.equals("BLACK")) {
            games.put(gameID, (games.get(gameID)).addBlackUser(username));
        }

    }
    public void clear() {
        games.clear();
    }
}
