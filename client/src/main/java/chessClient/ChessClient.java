package chessClient;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import request.*;
import response.*;
import server.ServerFacade;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessClient {
    private final String url;
    private final ServerFacade server;
    private boolean loggedIn;
    private String authToken;
    private String username;

    public ChessClient(String myURL) {
        this.url = myURL;
        this.server = new ServerFacade(this.url);
        this.loggedIn = false;
        this.authToken = null;
        this.username = null;
    }

    public String eval(String input) {
        String[] inputArray = input.split(" ");
        try {
            if(!loggedIn) {
                return switch(inputArray[0]) {
                    case "register" -> this.registerUser(inputArray);
                    case "login" -> this.loginUser(inputArray);
                    case "quit" -> "quit";
                    default -> this.help();
                };
            }
            else {
                return switch(inputArray[0]) {
                    case "logout" -> this.logoutUser(inputArray);
                    case "create" -> this.createGame(inputArray);
                    case "list" -> this.listGames();
                    case "join", "observe" -> this.joinGame(inputArray);
                    case "quit" -> "quit";
                    default -> this.help();
                };
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public String help() {
        if(!loggedIn) {
            return SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" +
                    SET_TEXT_COLOR_MAGENTA + " - to create an account\n" +
                    SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" +
                    SET_TEXT_COLOR_MAGENTA + " - to play chess\n" +
                    SET_TEXT_COLOR_BLUE + "quit" +
                    SET_TEXT_COLOR_MAGENTA + " - playing chess\n" +
                    SET_TEXT_COLOR_BLUE + "help" +
                    SET_TEXT_COLOR_MAGENTA + " - with possible commands";
        }
        else {
            return SET_TEXT_COLOR_BLUE + "create <NAME>" +
                    SET_TEXT_COLOR_MAGENTA + " - a game\n" +
                    SET_TEXT_COLOR_BLUE + "list" +
                    SET_TEXT_COLOR_MAGENTA + " - games\n" +
                    SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK|<empty>]" +
                    SET_TEXT_COLOR_MAGENTA + " - a game\n" +
                    SET_TEXT_COLOR_BLUE + "observe <ID>" +
                    SET_TEXT_COLOR_MAGENTA + " - a game\n" +
                    SET_TEXT_COLOR_BLUE + "logout" +
                    SET_TEXT_COLOR_MAGENTA + " - when you are done\n" +
                    SET_TEXT_COLOR_BLUE + "quit" +
                    SET_TEXT_COLOR_MAGENTA + " - playing chess\n" +
                    SET_TEXT_COLOR_BLUE + "help" +
                    SET_TEXT_COLOR_MAGENTA + " - with possible commands";
        }
    }

    public String registerUser(String[] inputArray) {
        UserData data;
        RegisterResponse response = null;
        switch(inputArray.length) {
            // TODO: Make this an error
            case 1, 2, 3 -> {
                    return "ERROR: please provide a username, password, and email. See help for more info";
            }
            default -> data = new UserData(inputArray[1], inputArray[2], inputArray[3]);
        }
        try {
            response = server.registerUser(data);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        loggedIn = true;
        if(response != null) {
            this.username = response.getUsername();
            this.authToken = response.getAuthToken();
            return "Logged in as " + response.getUsername();
        }
        return "";
    }

    public String loginUser(String[] inputArray) {
        LoginRequest request;
        LoginResponse response = null;
        switch(inputArray.length) {
            case 1, 2 -> {
                return "ERROR: please provide a username and password. See help for more details.";
            }
            default -> request = new LoginRequest(inputArray[1], inputArray[2]);
        }
        try {
            response = server.loginUser(request);
            loggedIn = true;
        }
        catch (Exception e) {
            // System.out.println(e.getMessage());
            if(e.getMessage().startsWith("Server returned HTTP response code: 401"))
                return "ERROR: incorrect login credentials. Please try again";
        }
        if(response != null) {
            this.username = response.getUsername();
            this.authToken = response.getAuthToken();
            return "Logged in as " + response.getUsername();
        }

        return "";
    }

    public String logoutUser(String[] inputArray) {
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResponse response = null;
        try {
            response = server.logoutUser(request);
            loggedIn = false;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
        return "logged out as " + this.username;
    }

    public String createGame(String[] inputArray) {
        CreateGameRequest request;
        CreateGameResponse response;
        if(inputArray.length == 2) {
            request = new CreateGameRequest(inputArray[1]);
        }
        else {
            // System.out.println(SET_TEXT_COLOR_RED + "Error: bad request");
            return SET_TEXT_COLOR_RED + "Error: bad request";
        }
        try {
            response = server.createGame(request, authToken);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "created game: " + inputArray[1];
    }

    public String listGames() {
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResponse response = null;
        try {
            response = server.listGames(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
        String games = "list of games:\n";
        for(GameData game : response.getGames()) {
            games += "Game Number " + game.gameID() + ": gameName: " + game.gameName() + " whiteUsername: " + game.whiteUsername() +
                    " blackUsername: " + game.blackUsername() + "\n";
        }
        return games;
    }

    public String joinGame(String[] inputArray) {
        JoinRequest request;
        String response;
        switch(inputArray.length) {
            case 2 -> request = new JoinRequest(authToken, null, inputArray[1]);
            case 3 -> request = new JoinRequest(authToken, inputArray[2], inputArray[1]);
            default -> request = new JoinRequest(authToken, null, null);
        }
        try {
            response = server.joinGame(request);
            loggedIn = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
        return response;
    }
}
