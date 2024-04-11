package chessClient;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import model.UserData;
import repl.GameHandler;
import request.*;
import response.*;
import server.ServerFacade;
import server.WebSocketFacade;

import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;


public class ChessClient {
    private final String url;
    private final ServerFacade server;

    private WebSocketFacade wsFacade;
    private boolean loggedIn;
    private String authToken;
    private int joinedGame;
    private ChessGame.TeamColor playerColor;
    private String username;

    private ChessGame game;

    public ChessClient(String myURL, GameHandler gameHandler) {
        this.url = myURL;
        this.server = new ServerFacade(this.url);
        this.loggedIn = false;
        this.authToken = null;
        this.username = null;
        try {
            this.wsFacade = new WebSocketFacade(this.getURL(), gameHandler);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getURL() {
        return url;
    }

    public void setJoinedGame(int joinedGame) {
        this.joinedGame = joinedGame;
    }

    public int getJoinedGame() {
        return joinedGame;
    }

    public WebSocketFacade getWsFacade() {
        return wsFacade;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public String getAuthToken() {
        return authToken;
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
        assert response != null;
        if(response.getMessage() != null && response.getMessage().equals("Error: already taken")) {
            return SET_TEXT_COLOR_RED + "Username is already taken";
        }
        else if(response.getMessage() != null){
            return SET_TEXT_COLOR_RED + response.getMessage();
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
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            if(e.getMessage().startsWith("Server returned HTTP response code: 401"))
                return "ERROR: incorrect login credentials. Please try again";
        }
        assert response != null;
        if(response.getMessage() != null && response.getMessage().equals("Error: unauthorized"))
            return "ERROR: incorrect login credentials. Please try again";
        if(response != null) {
            loggedIn = true;
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
            games += "Game Number " + game.gameID() + ": gameName: " + game.gameName() +
                    " whiteUsername: " + game.whiteUsername() + " blackUsername: " + game.blackUsername() + "\n";
        }
        return games;
    }

    public String joinGame(String[] inputArray) {
        JoinGameRequest request;
        String response;
        Response res;
        switch(inputArray.length) {
            case 2 -> {
                request = new JoinGameRequest(null, inputArray[1]);
                this.playerColor = null;
            }
            case 3 -> {
                request = new JoinGameRequest(inputArray[2], inputArray[1]);
                if(Objects.equals(inputArray[2], "BLACK"))
                    this.setPlayerColor(ChessGame.TeamColor.BLACK);
                else
                    this.setPlayerColor(ChessGame.TeamColor.WHITE);
            }
            default -> {
                return SET_TEXT_COLOR_RED + "Error: Please follow the format specified in the help section.";
            }
        }
        try {
            res = server.joinGame(request, authToken);
            if(res.getMessage() != null)
                throw new Exception(res.getMessage());
        }
        catch (Exception e) {
            if(e.getMessage().equals("Error: bad request"))
                System.out.print(SET_TEXT_COLOR_RED + "Please enter a valid game");
            if(e.getMessage().equals("Error: already taken"))
                System.out.println(SET_TEXT_COLOR_RED + "Error: Color is already taken. Please specify a different game or color.");
            return "";
        }
        this.setJoinedGame(Integer.parseInt(inputArray[1]));
        return "joined game successfully";
    }

    public String evalGameplay(String line) {
        String[] inputArray = line.split(" ");
        return switch(inputArray[0]) {
            case "help" -> this.helpGameplay();
            case "leave" -> "leave";
            case "resign" -> "resign";
            case "quit" -> "quit";
            case "redraw" -> "redraw";
            case "move", "highlight" -> line;
            default -> SET_TEXT_COLOR_RED + "Error. Use help for possible inputs";
        };
    }

    public String helpGameplay() {
        return SET_TEXT_COLOR_BLUE + "redraw" +
                SET_TEXT_COLOR_MAGENTA + " - redraw current chess board\n" +
                SET_TEXT_COLOR_BLUE + "leave" +
                SET_TEXT_COLOR_MAGENTA + " - return to post-login\n" +
                SET_TEXT_COLOR_BLUE + "move <current space><space to move to>" +
                SET_TEXT_COLOR_MAGENTA + " - moves piece at <current space> to <space to move to>\n" +
                SET_TEXT_COLOR_BLUE + "resign" +
                SET_TEXT_COLOR_MAGENTA + " - forefit the game and the game ends\n" +
                SET_TEXT_COLOR_BLUE + "highlight <specific space>" +
                SET_TEXT_COLOR_MAGENTA + " - highlights legal moves at a given <specific space>\n" +
                SET_TEXT_COLOR_BLUE + "help" +
                SET_TEXT_COLOR_MAGENTA + " - with possible commands";
    }

    public void redrawBoard() {
        // String[] headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        ChessBoard board = game.getBoard();
        if(this.playerColor == null || this.playerColor == ChessGame.TeamColor.WHITE) {
            System.out.println();
            printHeaders();
            printWhiteBoard(board);
            printHeaders();
        }
        else {
            System.out.println(SET_BG_COLOR_BLACK);
            printHeadersBackwards();
            printBlackBoard(board);
            printHeadersBackwards();
        }
    }

    public void redrawBoard(Collection<ChessMove> validMoves) {
        // String[] headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        ChessBoard board = game.getBoard();
        if(this.playerColor == null || this.playerColor == ChessGame.TeamColor.WHITE) {
            printHighlightedWhiteBoard(board, validMoves);
        }
        else {
            System.out.println(SET_BG_COLOR_BLACK);
            printHeadersBackwards();
            printHighlightedBlackBoard(board, validMoves);
            printHeadersBackwards();
        }
    }

    private void printHighlightedWhiteBoard(ChessBoard board, Collection<ChessMove> validMoves) {
        System.out.println();
        printHeaders();
        // System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
        int loopColor = 0;
        for(int x = 8; x >= 1; x--) {
            // loopColor = printLine(x, loopColor, board);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 1; y <= 8; y++) {
                setColorsAndPrintHighlights(board, x, y, loopColor, validMoves);
                loopColor++;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
        printHeaders();
    }

    private void printHighlightedBlackBoard(ChessBoard board, Collection<ChessMove> validMoves) {
        int loopColor = 0;
        for(int x = 1; x <= 8; x++) {
            // loopColor = printLine(x, loopColor, board);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 8; y >= 1; y--) {
                setColorsAndPrintHighlights(board, x, y, loopColor, validMoves);
                loopColor++;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
    }

    private void printHeaders() {
        String[] headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "     " + SET_TEXT_BOLD);
        for (String header : headers) {
            System.out.print(SET_TEXT_COLOR_BLACK + "  " + header + "  ");
        }
        System.out.println("     " + SET_BG_COLOR_BLACK);
    }

    private void printWhiteBoard(ChessBoard board) {
        int loopColor = 0;
        for(int x = 8; x >= 1; x--) {
            loopColor = printLine(x, loopColor, board);
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
    }

    private void setColorsAndPrint(ChessBoard board, int x, int y, int loopColor) {
        // Gives alternating color cool look
        if(loopColor % 2 == 0)
            System.out.print(SET_BG_COLOR_WHITE);
        else
            System.out.print(SET_BG_COLOR_DARK_GREY);

        // Decides color to print
        if(board.getPiece(new ChessPosition(x, y)) != null && board.getPiece(new ChessPosition(x, y)).getTeamColor().equals(ChessGame.TeamColor.WHITE))
            System.out.print(SET_TEXT_COLOR_RED);
        else
            System.out.print(SET_TEXT_COLOR_BLUE);

        // Print piece
        printPiece(board, new ChessPosition(x, y));
    }

    private void setColorsAndPrintHighlights(ChessBoard board, int x, int y, int loopColor, Collection<ChessMove> validMoves) {
        // Gives alternating color cool look
        if(loopColor % 2 == 0)
            System.out.print(SET_BG_COLOR_WHITE);
        else
            System.out.print(SET_BG_COLOR_DARK_GREY);

        // If is a valid move, Set bg color to yellow
        ChessPosition position = new ChessPosition(x, y);
        for(var move : validMoves) {
            if(move.getEndPosition().equals(position))
                System.out.print(SET_BG_COLOR_YELLOW);
            if(move.getStartPosition().equals(position))
                System.out.print(SET_BG_COLOR_GREEN);
        }

        // Decides color to print
        if(board.getPiece(new ChessPosition(x, y)) != null && board.getPiece(new ChessPosition(x, y)).getTeamColor().equals(ChessGame.TeamColor.WHITE))
            System.out.print(SET_TEXT_COLOR_RED);
        else
            System.out.print(SET_TEXT_COLOR_BLUE);

        // Print piece
        printPiece(board, new ChessPosition(x, y));
    }

    public void printPiece(ChessBoard board, ChessPosition position) {
        if(board.getPiece(position) == null) {
            System.out.print("     ");
            return;
        }
        switch(board.getPiece(position).getPieceType()) {
            case KING:
                System.out.print("  K  ");
                break;
            case QUEEN:
                System.out.print("  Q  ");
                break;
            case BISHOP:
                System.out.print("  B  ");
                break;
            case KNIGHT:
                System.out.print("  N  ");
                break;
            case ROOK:
                System.out.print("  R  ");
                break;
            case PAWN:
                System.out.print("  P  ");
                break;
        }
    }

    private void printHeadersBackwards() {
        String[] headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "     " + SET_TEXT_BOLD);
        for (int x = 7; x >= 0; x--) {
            System.out.print(SET_TEXT_COLOR_BLACK + "  " + headers[x] + "  ");
        }
        System.out.println("     " + SET_BG_COLOR_BLACK);
    }

    private void printBlackBoard(ChessBoard board) {
        int loopColor = 0;
        for(int x = 1; x <= 8; x++) {
            // loopColor = printLine(x, loopColor, board);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 8; y >= 1; y--) {
                setColorsAndPrint(board, x, y, loopColor);
                loopColor++;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
    }

    private int printLine(int x, int loopColor, ChessBoard board) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
        for(int y = 1; y <= 8; y++) {
            setColorsAndPrint(board, x, y, loopColor);
            loopColor++;
        }
        return loopColor;
    }
}
