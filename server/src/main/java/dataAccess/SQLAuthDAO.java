package dataAccess;

import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {


    public SQLAuthDAO() {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        try(var conn = DatabaseManager.getConnection()) {
            var createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS auths(authToken VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL, PRIMARY KEY(authToken));");
            createTable.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try(var conn = DatabaseManager.getConnection()) {
            if(getAuth(authToken) != null && getAuth(authToken).authToken().equals(authToken))
                throw new DataAccessException("Error: already taken");
            var createAuthToken = conn.prepareStatement("INSERT into auths(authToken, username) VALUES(?, ?);");
            createAuthToken.setString(1, authToken);
            createAuthToken.setString(2, username);
            createAuthToken.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String password = null;
        String email = null;
        String username = null;
        try(var conn = DatabaseManager.getConnection()) {
            // SQL Statement
            var getAuthStatement = conn.prepareStatement("SELECT authToken, username FROM auths WHERE authToken = ?");
            getAuthStatement.setString(1, authToken);
            var response = getAuthStatement.executeQuery();

            // Get the username from response
            if(response.next())
                username = response.getString("username");
            else
                return null;

        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return new AuthData(authToken, username);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            if(getAuth(authToken) == null)
                throw new DataAccessException("Error: bad request");
            var deleteAuthStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken= ?;");
            deleteAuthStatement.setString(1, authToken);
            deleteAuthStatement.executeUpdate();
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() {
        try(var conn = DatabaseManager.getConnection()) {
            var clearStatement = conn.prepareStatement("TRUNCATE auths");
            clearStatement.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
