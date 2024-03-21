package response;

import model.GameData;

import java.util.ArrayList;

public class ListGamesResponse extends Response{

    private ArrayList<GameData> games;
    public ListGamesResponse(String message, ArrayList<GameData> games) {
        super(message);
        this.games = games;
    }

    public ArrayList<GameData> getGames() {
        return games;
    }
}
