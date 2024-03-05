package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
import java.util.Objects;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() {
        try(var conn = DatabaseManager.getConnection()) {
            var createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users(username VARCHAR(255), password VARCHAR(255), email VARCHAR(255));");
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
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String password = null;
        String email = null;
        try(var conn = DatabaseManager.getConnection()) {
            // SQL Statement
            var getUserStatement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?");
            getUserStatement.setString(1, username);
            var response = getUserStatement.executeQuery();

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

    // Password encoder
    private String encodeUserPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);

        // write the hashed password in database along with the user's other information
        // writeHashedPasswordToDatabase(username, hashedPassword);
    }

    // Password decoder
    boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        // var hashedPassword = readHashedPasswordFromDatabase(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // return encoder.matches(providedClearTextPassword, getUser(username).password());
        return true;
    }
}
