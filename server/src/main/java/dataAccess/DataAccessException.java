package dataAccess;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
    public String getException(Response res) {
        var serializer = new Gson();
        if(getMessage().equals("Error: bad request")) {
            res.status(400);
            ErrorMessage message = new ErrorMessage(getMessage());
            return serializer.toJson(message);
        }
        else if(getMessage().equals("Error: unauthorized")) {
            res.status(401);
            ErrorMessage message = new ErrorMessage(getMessage());
            return serializer.toJson(message);
        }
        else if(getMessage().equals("Error: already taken")) {
            res.status(403);
            ErrorMessage message = new ErrorMessage(getMessage());
            return serializer.toJson(message);
        }
        else {
            res.status(500);
            ErrorMessage message = new ErrorMessage("Error: " + getMessage());
            return serializer.toJson(message);
        }
    }
}
