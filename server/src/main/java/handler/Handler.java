package handler;
import spark.Request;
import spark.Response;

public abstract class Handler {
    public abstract String handle(Request req, Response res);
}
