package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import request.LoginRequest;
import response.LoginResponse;
import response.RegisterResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    @Override
    public String handle(Request req, Response res) {
        // Deserializing json object into LoginRequest Object
        var serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(req.body(), LoginRequest.class);

        // Calling Login Service
        UserService service = new UserService();
        String authToken = null;
        try {
            authToken = service.login(loginRequest);
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
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
        LoginResponse response = new LoginResponse(null, authToken, loginRequest.username());
        res.status(200);
        return serializer.toJson(response);
    }
}
