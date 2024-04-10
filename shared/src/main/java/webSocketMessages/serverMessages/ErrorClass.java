package webSocketMessages.serverMessages;

public class ErrorClass extends ServerMessage{

    private String errorMessage;
    public ErrorClass(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
