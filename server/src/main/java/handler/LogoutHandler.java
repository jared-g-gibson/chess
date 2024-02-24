package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import dataAccess.UserDAO;
import request.LoginRequest;
import request.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class LogoutHandler extends Handler {
    private AuthDAO auths;
    private UserDAO users;
    public LogoutHandler(AuthDAO auths, UserDAO users) {
        this.auths = auths;
        this.users = users;
    }
    @Override
    public String handle(Request req, Response res) {
        var serializer = new Gson();
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
        UserService service = new UserService(auths, users);

        try {
            service.logout(logoutRequest.authToken());
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
        res.status(200);
        return "{}";
    }
}
