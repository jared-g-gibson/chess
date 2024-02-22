package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.ErrorMessage;
import service.ClearService;
import spark.Request;
import spark.Response;

public class DeleteHandler extends Handler {
    public String handle(Request req, Response res) {
        ClearService clear = new ClearService();
        var serializer = new Gson();
        try {
            clear.clear();
        } catch (DataAccessException e) {
            res.status(500);
            ErrorMessage message = new ErrorMessage("Error: " + e.getMessage());
            return serializer.toJson(message);
        }
        res.status(200);
        return "{}";
    }
}
