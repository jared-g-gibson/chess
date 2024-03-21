package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        try(var conn = DatabaseManager.getConnection()) {
            var createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS games(gameID int AUTO_INCREMENT, whiteUsername VARCHAR(255), blackUsername VARCHAR(255), gameName VARCHAR(255), game BLOB, PRIMARY KEY(gameID) );");
            createTable.executeUpdate();
            // numGames = 0;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            //if(getGameFromGameName(gameData.gameName()) != null)
                //throw new DataAccessException("Error: bad request");
            if(gameData.gameName() == null)
                throw new DataAccessException("Error: bad request");
            var createGameStatement = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?);");
            createGameStatement.setString(1, gameData.whiteUsername());
            createGameStatement.setString(2, gameData.blackUsername());
            createGameStatement.setString(3, gameData.gameName());
            var json = new Gson().toJson(gameData.game());
            createGameStatement.setString(4, json);
            createGameStatement.executeUpdate();
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException{
        int game = Integer.parseInt(gameID);
        try(var conn = DatabaseManager.getConnection()) {
            var getGameStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?");
            getGameStatement.setInt(1, game);
            var response = getGameStatement.executeQuery();
            if(response.next()) {
                var chessGame = new Gson().fromJson(response.getString("game"), ChessGame.class);
                return new GameData(response.getInt("gameID"), response.getString("whiteUsername"),
                        response.getString("blackUsername"), response.getString("gameName"), chessGame);
            }
            else
                return null;

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
    public GameData getGameFromGameName(String gameName) throws DataAccessException {
        ArrayList<GameData> games;
        try {
            games = getGames();
            for(GameData game : games) {
                if(game.gameName().equals(gameName))
                    return game;
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateGame(String color, String username, String gameID) throws DataAccessException {
        GameData game = getGame(gameID);
        if(color.equals("WHITE")) {
            if(game.whiteUsername() != null)
                throw new DataAccessException("Error: already taken");
            try(var conn = DatabaseManager.getConnection()) {
                var updateGameStatement = conn.prepareStatement("UPDATE games SET whiteUsername = ? WHERE gameID = ?;");
                updateGameStatement.setString(1, username);
                updateGameStatement.setInt(2, Integer.parseInt(gameID));
                updateGameStatement.executeUpdate();
            }
            catch (Exception e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        else if(color.equals("BLACK")) {
            if(game.blackUsername() != null)
                throw new DataAccessException("Error: already taken");
            try(var conn = DatabaseManager.getConnection()) {
                var updateGameStatement = conn.prepareStatement("UPDATE games SET blackUsername = ? WHERE gameID = ?;");
                updateGameStatement.setString(1, username);
                updateGameStatement.setInt(2, Integer.parseInt(gameID));
                updateGameStatement.executeUpdate();
            }
            catch (Exception e) {
                throw new DataAccessException(e.getMessage());
            }
        }
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
        int numGames = 0;
        try {
            ArrayList<GameData> games = getGames();
            for(GameData game : games) {
                numGames++;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return numGames;
    }
}
