package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{

    private int numGames;

    public SQLGameDAO() {
        try(var conn = DatabaseManager.getConnection()) {
            var createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS games(gameID int AUTO_INCREMENT, whiteUsername VARCHAR(255), blackUsername VARCHAR(255), gameName VARCHAR(255), game BLOB, PRIMARY KEY(gameID) );");
            createTable.executeUpdate();
            numGames = 0;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var createGameStatement = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?);");
            createGameStatement.setString(1, gameData.whiteUsername());
            createGameStatement.setString(2, gameData.blackUsername());
            createGameStatement.setString(3, gameData.gameName());
            var json = new Gson().toJson(gameData.game());
            createGameStatement.setString(4, json);
            createGameStatement.executeUpdate();
            numGames++;
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException{
        int game = Integer.parseInt(gameID);
        try(var conn = DatabaseManager.getConnection()) {
            var getGameStatement = conn.prepareStatement("SELECT gameID FROM games WHERE gameID = ?");
            getGameStatement.setInt(1, game);
            var response = getGameStatement.executeQuery();
            var chessGame = new Gson().fromJson(response.getString("game"), ChessGame.class);
            return new GameData(response.getInt("gameID"), response.getString("whiteUsername"),
                    response.getString("blackUsername"), response.getString("gameName"), chessGame);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()) {
            var getGamesStatement = conn.prepareStatement("SELECT * FROM games");
            var response = getGamesStatement.executeQuery();
            while(response.next()) {
                ChessGame chessGame = new Gson().fromJson(response.getString("game"), ChessGame.class);

                // White Username
                String whiteUsername = response.getString("whiteUsername");

                // Black Username
                String blackUsername = response.getString("blackUsername");

                gameList.add(new GameData(response.getInt("gameID"), whiteUsername,
                        blackUsername, response.getString("gameName"), chessGame));
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return gameList;
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
        return numGames;
    }
}
