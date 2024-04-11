package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import request.JoinRequest;
import service.GameService;
import webSocketMessages.serverMessages.ErrorClass;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.xml.crypto.Data;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    ConnectionManager connections;
    private AuthDAO auths;
    private GameDAO games;
    private UserDAO users;

    public WebSocketHandler(AuthDAO auths, GameDAO games, UserDAO users) {
        this.auths = auths;
        this.games = games;
        this.users = users;
        connections = new ConnectionManager();
    }



    public String handle() {
        return "";
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
                this.joinPlayer(joinPlayer, session);
            }
            case JOIN_OBSERVER -> {
                JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
                this.joinObserver(joinObserver, session);
                LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, 1);
                String res = new Gson().toJson(loadGame);
                session.getRemote().sendString(res);
            }
        }
        System.out.printf("Received: %s", command.toString());
        // session.getRemote().sendString("WebSocket response: " + message);
    }

    public void joinPlayer(JoinPlayer joinPlayer, Session session) {
        // JoinRequest joinRequest = new JoinRequest( joinPlayer.getAuthString(), joinPlayer.getPlayerColor().toString(), Integer.toString(joinPlayer.getGameID()));
        try {
            String username = null;
            // Invalid authToken
            if(joinPlayer.getAuthString() == null) {
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Getting the username
            try {
                username = auths.getAuth(joinPlayer.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Check if gameID is valid
            try {
                GameData game = games.getGame(Integer.toString(joinPlayer.getGameID()));
                if(game == null)
                    throw new DataAccessException("Error: invalid game");
            }
            catch (DataAccessException e){
                sendError(session, "Error: Invalid game");
                return;
            }

            // Check if username matches (don't steal someone's invitation)
            if(joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                if(games.getGame(Integer.toString(joinPlayer.getGameID())).blackUsername() != null && !username.equals(games.getGame(Integer.toString(joinPlayer.getGameID())).blackUsername())) {
                    sendError(session, "Error: Username already taken");
                    return;
                }
            }
            else {
                if(games.getGame(Integer.toString(joinPlayer.getGameID())).whiteUsername() != null && !username.equals(games.getGame(Integer.toString(joinPlayer.getGameID())).whiteUsername())) {
                    sendError(session, "Error: Username already taken");
                    return;
                }
            }

            // Adding user to game using service
            // GameService service = new GameService(auths, games);
            // service.joinGame(joinRequest);

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the game as " + joinPlayer.getPlayerColor().toString());
            var json = new Gson().toJson(message);

            // Broadcast message to clients
            // ConnectionManager broadcast = new ConnectionManager();
            connections.add(joinPlayer.getGameID(), joinPlayer.getAuthString(), session);
            connections.broadcast(joinPlayer.getGameID(), json, joinPlayer.getAuthString());
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, 1);
            String res = new Gson().toJson(loadGame);
            session.getRemote().sendString(res);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendError(Session session, String errorMessage) throws IOException {
        ErrorClass error = new ErrorClass(ServerMessage.ServerMessageType.ERROR, errorMessage);
        String message = new Gson().toJson(error);
        session.getRemote().sendString(message);
    }

    public void joinObserver(JoinObserver joinObserver, Session session) {
        // JoinRequest joinRequest = new JoinRequest( joinPlayer.getAuthString(), joinPlayer.getPlayerColor().toString(), Integer.toString(joinPlayer.getGameID()));
        try {
            // Getting the username
            String username = auths.getAuth(joinObserver.getAuthString()).username();

            // Adding user to game using service
            // GameService service = new GameService(auths, games);
            // service.joinGame(joinRequest);

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the game as an observer.");
            var json = new Gson().toJson(message);

            // Broadcast message to clients
            // ConnectionManager broadcast = new ConnectionManager();
            connections.add(joinObserver.getGameID(), joinObserver.getAuthString(), session);
            connections.broadcast(joinObserver.getGameID(), json, joinObserver.getAuthString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(int gameID, String message, String authToken) {

    }

    public void broadcastMessage(int gameID, String message, String exceptThisAuthToken) throws IOException {

    }

    /*@OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(Session session) {}*/

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {}


}
