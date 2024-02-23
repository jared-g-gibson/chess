package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import dataAccess.UserDAO;
import model.UserData;
import response.RegisterResponse;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class RegisterHandler extends Handler {

    private AuthDAO auths;
    private UserDAO users;

    public RegisterHandler(AuthDAO auths, UserDAO users) {
        this.auths = auths;
        this.users = users;
    }
    public String handle(Request req, Response res) {
        // Use gson to convert req.body to RegisterRequest
        // Deserializes data from request
        var serializer = new Gson();
        var userData = serializer.fromJson(req.body(), UserData.class);

        // Passes userData to register service for more processing
        UserService service = new UserService(auths, users);
        String authToken = null;
        try {
            if(userData.username() == null || userData.email() == null || userData.password() == null)
                throw new DataAccessException("Error: bad request");
            authToken = service.register(userData);
        } catch (DataAccessException e) {
            if(Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
                ErrorMessage message = new ErrorMessage(e.getMessage());
                return serializer.toJson(message);
            }
            else if(Objects.equals(e.getMessage(), "Error: already taken")) {
                res.status(403);
                ErrorMessage message = new ErrorMessage(e.getMessage());
                return serializer.toJson(message);
            }
            else {
                res.status(500);
                ErrorMessage message = new ErrorMessage("Error: " + e.getMessage());
                return serializer.toJson(message);
            }
        }

        // Serializes username and authtoken back in json
        RegisterResponse response = new RegisterResponse(null, authToken, userData.username());
        res.status(200);
        return serializer.toJson(response);
    }
}
