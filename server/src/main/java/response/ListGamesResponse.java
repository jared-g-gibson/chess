package response;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGamesResponse extends Response{

    private ArrayList<GameData> games;
    public ListGamesResponse(String message, ArrayList<GameData> games) {
        super(message);
        this.games = games;
    }
}
