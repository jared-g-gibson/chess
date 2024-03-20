package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import request.LoginRequest;
import response.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private AuthDAO auths;
    private UserDAO users;
    public LoginHandler(AuthDAO auths, UserDAO users) {
        this.auths = auths;
        this.users = users;
    }
    @Override
    public String handle(Request req, Response res) {
        // Deserializing json object into LoginRequest Object
        var serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(req.body(), LoginRequest.class);

        // Calling Login Service
        UserService service = new UserService(auths, users);
        String authToken = null;
        try {
            authToken = service.login(loginRequest);
        } catch (DataAccessException e) {
            return e.getException(res);
        }

        // Serializes username and authToken back in json
        LoginResponse response = new LoginResponse(null, authToken, loginRequest.username());
        res.status(200);
        return serializer.toJson(response);
    }
}
