package server;

import dataAccess.*;
import handler.*;
import org.eclipse.jetty.util.log.Log;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Data Access Objects
        UserDAO users = new MemoryUserDao();
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();


        // Register your endpoints and handle exceptions here.
        // Clear Endpoint
        DeleteHandler deleteHandler = new DeleteHandler(auths, games, users);
        Spark.delete("/db", ((request, response) -> deleteHandler.handle(request, response)));
        // Register Endpoint
        RegisterHandler registerHandler = new RegisterHandler(auths, users);
        Spark.post("/user", (request, response) -> registerHandler.handle(request, response));
        // Login Endpoint
        LoginHandler loginHandler = new LoginHandler(auths, users);
        Spark.post("/session", (request, response) -> loginHandler.handle(request, response));
        // Logout Endpoint
        LogoutHandler logoutHandler = new LogoutHandler(auths, users);
        Spark.delete("/session", (request, response) -> logoutHandler.handle(request, response));
        // Create Game Endpoint
        CreateGameHandler createGameHandler = new CreateGameHandler(auths, games);
        Spark.post("/game", (request, response) -> createGameHandler.handle(request, response));
        // Join Game
        JoinGameHandler joinGameHandler = new JoinGameHandler(auths, games);
        Spark.put("/game", (request, response) -> joinGameHandler.handle(request, response));
        // List Games

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
