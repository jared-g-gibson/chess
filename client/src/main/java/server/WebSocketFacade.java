package server;

import com.google.gson.Gson;
import exception.ResponseException;
import repl.GameHandler;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;

import javax.management.Notification;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private Session session;
    private GameHandler gameHandler;

    public WebSocketFacade(String url) throws ResponseException, URISyntaxException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // Set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });

        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


    // Empty
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    // Empty
    @Override
    public void onClose(Session session, CloseReason closeReason) {
    }

    // Empty
    @Override
    public void onError(Session session, Throwable th) {
    }

    public void joinPlayer() {

    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onMessage(ServerMessage message) {

        // 1. Deserialize the message

        // 2. Call the gameHandler

    }
}
