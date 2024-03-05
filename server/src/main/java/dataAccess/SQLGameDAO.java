package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {
        try(var conn = DatabaseManager.getConnection()) {
            var createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS games(gameID int AUTO_INCREMENT, whiteUsername VARCHAR(255), blackUsername VARCHAR(255), gameName VARCHAR(255), game BLOB, PRIMARY KEY(gameID) );");
            createTable.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public GameData getGame(String gameID) {
        return null;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public GameData getGameFromGameName(String gameName) {
        return null;
    }

    @Override
    public void updateGame(String color, String username, String gameID) {

    }

    @Override
    public void clear() {
        try(var conn = DatabaseManager.getConnection()) {
            var clearStatement = conn.prepareStatement("TRUNCATE games");
            clearStatement.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getNumGames() {
        return 0;
    }
}
