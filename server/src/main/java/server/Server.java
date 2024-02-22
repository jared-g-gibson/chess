package server;

import dataAccess.MemoryUserDao;
import handler.DeleteHandler;
import handler.LoginHandler;
import handler.RegisterHandler;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // Clear Endpoint
        DeleteHandler deleteHandler = new DeleteHandler();
        Spark.delete("/db", ((request, response) -> deleteHandler.handle(request, response)));
        // Register Endpoint
        RegisterHandler registerHandler = new RegisterHandler();
        Spark.post("/user", (request, response) -> registerHandler.handle(request, response));
        // Login Endpoint
        LoginHandler loginHandler = new LoginHandler();
        Spark.post("/session", (request, response) -> loginHandler.handle(request, response));

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
