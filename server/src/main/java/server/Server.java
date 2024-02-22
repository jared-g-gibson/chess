package server;

import dataAccess.MemoryUserDao;
import handler.DeleteHandler;
import handler.RegisterHandler;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        DeleteHandler deleteHandler = new DeleteHandler();
        Spark.delete("/db", ((request, response) -> deleteHandler.handle(request, response)));
        RegisterHandler registerHandler = new RegisterHandler();
        Spark.post("/user", (request, response) -> registerHandler.handle(request, response));

        Spark.init();
        // Spark.delete("/db", ((request, response) -> null));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
