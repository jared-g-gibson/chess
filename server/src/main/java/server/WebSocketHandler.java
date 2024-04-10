package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import request.JoinRequest;
import service.GameService;
import webSocketMessages.userCommands.JoinPlayer;

import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {

    ArrayList<WebSocketSessions> sessions;

    public WebSocketHandler() {
        sessions = new ArrayList<>();
    }



    public String handle() {
        return "";
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }

    public String joinPlayer(JoinPlayer message) {
        JoinRequest joinRequest = new JoinRequest( message.getAuthString(), message.getPlayerColor().toString(), Integer.toString(message.getGameID()));
        return "";
    }

    public void sendMessage(int gameID, String message, String authToken) {

    }

    public void broadcastMessage(int gameID, String message, String exceptThisAuthToken) {

    }

    @OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(Session session) {}

    @OnWebSocketError
    public void onError(Throwable throwable) {}


}
