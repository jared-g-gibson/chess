package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {

    private String message;
    public Notification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }

    public String getMessage() {
        return message;
    }
}
