package response;

public class CreateGameResponse extends Response {

    private final String gameID;
    public CreateGameResponse(String message, String gameID) {
        super(message);
        this.gameID = gameID;
    }
}
