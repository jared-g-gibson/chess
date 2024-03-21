package chessClient;

import com.google.gson.Gson;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;
import server.ServerFacade;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_MAGENTA;


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
            case 2 -> data = new UserData(inputArray[1], null, null);
            case 3 -> data = new UserData(inputArray[1], inputArray[2], null);
            case 4 -> data = new UserData(inputArray[1], inputArray[2], inputArray[3]);
            default -> data = new UserData(null, null, null);
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
            case 2 -> request = new LoginRequest(inputArray[1], null);
            case 3 -> request = new LoginRequest(inputArray[1], inputArray[2]);
            default -> request = new LoginRequest(null, null);
        }
        try {
            response = server.loginUser(request);
            loggedIn = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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
}
