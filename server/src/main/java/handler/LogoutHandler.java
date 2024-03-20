package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import request.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;

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
            return e.getException(res);
        }
        res.status(200);
        return "{}";
    }
}
