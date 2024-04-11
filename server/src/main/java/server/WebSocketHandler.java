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
import webSocketMessages.userCommands.*;

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
            }
            case MAKE_MOVE -> {
                MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                this.makeMove(makeMove, session);
            }
            case LEAVE -> {
                Leave leave = new Gson().fromJson(message, Leave.class);
                this.leave(leave, session);
            }
            case RESIGN -> {
                Resign resign = new Gson().fromJson(message, Resign.class);
                this.resign(resign, session);
            }
        }
        System.out.printf("Received: %s", command.toString());
        // session.getRemote().sendString("WebSocket response: " + message);
    }

    private void makeMove(MakeMove makeMove, Session session) {
    }

    private void leave(Leave leave, Session session) {
        try {
            String username;
            // Getting the username
            try {
                username = auths.getAuth(leave.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game");
            var json = new Gson().toJson(message);

            // Remove from Database
            if(username.equals(games.getGame(Integer.toString(leave.getGameID())).whiteUsername())) {
                games.removePlayer(Integer.toString(leave.getGameID()), ChessGame.TeamColor.WHITE);
            }
            else if(username.equals(games.getGame(Integer.toString(leave.getGameID())).blackUsername())) {
                games.removePlayer(Integer.toString(leave.getGameID()), ChessGame.TeamColor.BLACK);
            }

            // Broadcast message to clients
            connections.broadcast(leave.getGameID(), json, leave.getAuthString());
            connections.removeSessionFromGame(leave.getGameID(), leave.getAuthString(), session);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void resign(Resign resign, Session session) {

    }

    public void joinPlayer(JoinPlayer joinPlayer, Session session) {
        try {
            if(joinPlayer.getPlayerColor() == null) {
                JoinObserver observer = new JoinObserver(joinPlayer.getAuthString(), joinPlayer.getGameID());
                joinObserver(observer, session);
                return;
            }
            String username;
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
                if(!username.equals(games.getGame(Integer.toString(joinPlayer.getGameID())).blackUsername())) {
                    sendError(session, "Error: Username already taken");
                    return;
                }
            }
            else {
                if(!username.equals(games.getGame(Integer.toString(joinPlayer.getGameID())).whiteUsername())) {
                    sendError(session, "Error: Username already taken");
                    return;
                }
            }

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the game as " + joinPlayer.getPlayerColor().toString());
            var json = new Gson().toJson(message);

            // Broadcast message to clients
            connections.add(joinPlayer.getGameID(), joinPlayer.getAuthString(), session);
            connections.broadcast(joinPlayer.getGameID(), json, joinPlayer.getAuthString());
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, games.getGame(Integer.toString(joinPlayer.getGameID())).game());
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
        try {
            String username;
            // Invalid authToken
            if(joinObserver.getAuthString() == null) {
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Getting the username
            try {
                username = auths.getAuth(joinObserver.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Check if gameID is valid
            try {
                GameData game = games.getGame(Integer.toString(joinObserver.getGameID()));
                if(game == null)
                    throw new DataAccessException("Error: invalid game");
            }
            catch (DataAccessException e){
                sendError(session, "Error: Invalid game");
                return;
            }

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
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, games.getGame(Integer.toString(joinObserver.getGameID())).game());
            String res = new Gson().toJson(loadGame);
            session.getRemote().sendString(res);
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
