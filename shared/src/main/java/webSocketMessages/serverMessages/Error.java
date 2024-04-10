package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    private String errorMessage;
    public Error(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }
}
