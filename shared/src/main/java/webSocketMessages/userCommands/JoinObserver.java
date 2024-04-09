package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {

    private int gameID;
    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
