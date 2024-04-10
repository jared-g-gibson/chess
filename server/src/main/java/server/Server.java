package server;

import dataAccess.*;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Data Access Objects
        UserDAO users = new SQLUserDAO();
        AuthDAO auths = new SQLAuthDAO();
        GameDAO games = new SQLGameDAO();


        // Register your endpoints and handle exceptions here.
        // WebSocket
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        Spark.webSocket("/connect", WebSocketHandler.class);
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
        ListGamesHandler listGamesHandler = new ListGamesHandler(auths, games);
        Spark.get("/game", (request, response) -> listGamesHandler.handle(request, response));

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
