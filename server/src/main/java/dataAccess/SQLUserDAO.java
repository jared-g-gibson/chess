package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.LoginRequest;

import java.sql.ResultSet;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        try(var conn = DatabaseManager.getConnection()) {
            var createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users(username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, PRIMARY KEY(username));");
            createTable.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void clear() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var clearStatement = conn.prepareStatement("TRUNCATE users");
            clearStatement.executeUpdate();
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var createUserStatement = conn.prepareStatement("INSERT into users(username, password, email) VALUES(?, ?, ?);");
            createUserStatement.setString(1, data.username());
            createUserStatement.setString(2, encodeUserPassword(data.password()));
            createUserStatement.setString(3, data.email());
            createUserStatement.executeUpdate();
        } catch (Exception e) {
            if(e.getMessage().length() > 13 && e.getMessage().substring(0, 15).equals("Duplicate entry"))
                throw new DataAccessException("Error: already taken");
            //if(e.getMessage().equals("Duplicate entry 'Joe' for key 'users.PRIMARY'"))
                //throw new DataAccessException("Error: already taken");
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String password = null;
        String email = null;
        try(var conn = DatabaseManager.getConnection()) {
            ResultSet response;
            // SQL Statement
            if (username.matches("[a-zA-Z]+")) {
                var getUserStatement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?");
                getUserStatement.setString(1, username);
                response = getUserStatement.executeQuery();
            }
            else
                return null;
            // Get the password and email from the response
            if(response.next()) {
                password = response.getString("password");
                email = response.getString("email");
            }
            else
                return null;

        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return new UserData(username, password, email);
    }

    @Override
    public boolean verifyUser(LoginRequest loginInfo) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            UserData user = getUser(loginInfo.username());

            // User does not exist
            if(user == null)
                throw new DataAccessException("Error: unauthorized");

            // Incorrect password
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(loginInfo.password(), user.password()))
                throw new DataAccessException("Error: unauthorized");
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        return true;
    }

    // Password encoder
    private String encodeUserPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);

        // write the hashed password in database along with the user's other information
        // writeHashedPasswordToDatabase(username, hashedPassword);
    }
}
