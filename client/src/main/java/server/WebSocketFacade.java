package server;

import com.google.gson.Gson;
import exception.ResponseException;
import repl.GameHandler;
import webSocketMessages.serverMessages.ErrorClass;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private Session session;
    private GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) throws ResponseException, URISyntaxException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // Set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    // System.out.println(message);
                    ServerMessage command = new Gson().fromJson(message, ServerMessage.class);
                    switch (command.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGame game = new Gson().fromJson(message, LoadGame.class);
                            gameHandler.updateGame(game.getGame());
                        }
                        case NOTIFICATION -> {
                            Notification notificationCommand = new Gson().fromJson(message, Notification.class);
                            gameHandler.printMessage(notificationCommand.getMessage());
                        }
                        case ERROR -> {
                            ErrorClass errorCommand = new Gson().fromJson(message, ErrorClass.class);
                            gameHandler.printMessage(errorCommand.getErrorMessage());
                        }
                    }
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

    public void joinPlayer(String json) throws ResponseException {
        try {
            this.send(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void joinObserver(String json) throws ResponseException {
        try {
            this.send(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMove(String json) throws ResponseException {
        try {
            this.send(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leave(String json) throws ResponseException {
        try {
            this.send(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resign(String json) throws ResponseException {
        try {
            this.send(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
}
