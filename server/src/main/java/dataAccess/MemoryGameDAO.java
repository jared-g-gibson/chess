package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<String, GameData> games = new HashMap<>();

    public void createGame(GameData gameData) throws DataAccessException{
        if(getGameFromGameName(gameData.gameName()) != null)
            throw new DataAccessException("Error: bad request");
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

    @Override
    public void removePlayer(String gameID, ChessGame.TeamColor teamColor) throws DataAccessException {

    }

    @Override
    public void updateGameState(String gameID, ChessGame updatedGame) throws DataAccessException {

    }

    public void updateGame(String color, String username, String gameID) throws DataAccessException {
        GameData game = getGame(gameID);
        if(color.equals("WHITE")) {
            if(game.whiteUsername() != null)
                throw new DataAccessException("Error: already taken");
            games.put(gameID, games.get(gameID).addWhiteUser(username));
        }
        else if(color.equals("BLACK")) {
            if(game.blackUsername() != null)
                throw new DataAccessException("Error: already taken");
            games.put(gameID, games.get(gameID).addBlackUser(username));
        }

    }
    public void clear() {
        games.clear();
    }
}
