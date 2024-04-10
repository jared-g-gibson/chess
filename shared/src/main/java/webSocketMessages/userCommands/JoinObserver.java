package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {

    private int gameID;
    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
