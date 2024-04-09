package server;

import com.google.gson.Gson;
import exception.ResponseException;

import javax.management.Notification;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private Session session;

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
                    Notification notification = new Gson().fromJson(message, Notification.class);
                }
            });

        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


        @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
