package repl;

import chess.*;
import chessClient.ChessClient;
import com.google.gson.Gson;
import exception.ResponseException;
import server.WebSocketFacade;
import webSocketMessages.userCommands.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayUI {
    private final String[] headers;
    private final ChessClient client;
    private final String[] initialRow;

    private ChessBoard board;
    private final String authToken;

    // Red is White Team
    // Blue is Black Team

    public GameplayUI(ChessClient client, String line) {
        this.client = client;
        this.authToken = client.getAuthToken();
        headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        initialRow = new String[]{"R", "N", "B", "Q", "K", "B", "N", "R"};
        try {
            joinAsPlayerOrObserver(line);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinAsPlayerOrObserver(String line) throws Exception{
        if(line.toLowerCase().startsWith("join") && line.split(" ").length == 3)
            joinPlayer();
        else
            joinObserver();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("leave")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.evalGameplay(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
                if(result.equals("redraw")) {
                    client.redrawBoard();
                }
                if(result.startsWith("highlight")) {
                    highlight(line);
                }
                if(result.startsWith("move")) {
                    makeMove(result);
                }
                if(result.startsWith("leave")) {
                    leave();
                }
                if(result.startsWith("resign")) {
                    resign();
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        // printBoard();
    }

    public void printPrompt() {
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\n[PLAYING_GAME] >>> " + SET_TEXT_COLOR_GREEN);
    }

    public void joinPlayer() throws ResponseException {
        JoinPlayer player = new JoinPlayer(client.getAuthToken(), client.getJoinedGame(), client.getPlayerColor());
        var json = new Gson().toJson(player);
        try {
            client.getWsFacade().joinPlayer(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void joinObserver() throws ResponseException {
        JoinObserver observer = new JoinObserver(client.getAuthToken(), client.getJoinedGame());
        var json = new Gson().toJson(observer);
        try {
            client.getWsFacade().joinObserver(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leave() throws ResponseException {
        Leave leave = new Leave(client.getAuthToken(), client.getJoinedGame());
        var json = new Gson().toJson(leave);
        try {
            client.getWsFacade().leave(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resign() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to resign? You will forfeit the game. Enter y or n\n");
        String line = scanner.nextLine();
        switch (line) {
            case "y" -> { }
            case "n" -> {
                return;
            }
            default -> {
                System.out.println("Error: follow the format given");
                this.resign();
            }
        }
        Resign resign = new Resign(client.getAuthToken(), client.getJoinedGame());
        var json = new Gson().toJson(resign);
        try {
            client.getWsFacade().resign(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


    public void highlight(String line) {
        String[] commandArray = line.split(" ");
        // Incorrect Formatting
        if(commandArray.length < 2 || commandArray[1].length() < 2) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }

        // Is valid position
        String start = commandArray[1];
        if(!(start.charAt(0) == 'a' || start.charAt(0) == 'b' || start.charAt(0) == 'c' || start.charAt(0) == 'd' ||
                start.charAt(0) == 'e' || start.charAt(0) == 'f' || start.charAt(0) == 'g' || start.charAt(0) == 'h')) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }

        // Get start and end positions from strings
        // Example a2a3 -> StartPosition: (1,2) EndPosition: (1, 3)
        ChessPosition startPosition = getPositionWhite(start);

        // Collection of valid moves
        Collection<ChessMove> validMoves = client.getGame().validMoves(startPosition);

        // Redraw the board with valid moves highlighted
        client.redrawBoard(validMoves);



    }

    public void makeMove(String command) throws ResponseException {
        String[] commandArray = command.split(" ");

        // Incorrect Formatting
        if(commandArray.length < 2 || commandArray[1].length() < 4) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }

        // Get Starting and Ending positions from command
        String start = commandArray[1].substring(0,2);
        String end = commandArray[1].substring(2);

        // Check if move is valid syntax
        if(!(start.charAt(0) == 'a' || start.charAt(0) == 'b' || start.charAt(0) == 'c' || start.charAt(0) == 'd' ||
                start.charAt(0) == 'e' || start.charAt(0) == 'f' || start.charAt(0) == 'g' || start.charAt(0) == 'h')) {
                System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
                return;
        }
        if(!(end.charAt(0) != 'a' || end.charAt(0) != 'b' || end.charAt(0) != 'c' || end.charAt(0) != 'd' ||
                end.charAt(0) != 'e' || end.charAt(0) != 'f' || end.charAt(0) != 'g' || end.charAt(0) != 'h')) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }

        // Get start and end positions from strings
        // Example a2a3 -> StartPosition: (1,2) EndPosition: (1, 3)
        ChessPosition startPosition = null;
        ChessPosition endPosition = null;
        if(client.getPlayerColor() != null && client.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            startPosition = getPosition(start);
            endPosition = getPosition(end);
        }
        else if(client.getPlayerColor() != null && client.getPlayerColor() == ChessGame.TeamColor.BLACK) {
            startPosition = getPosition(start);
            endPosition = getPosition(end);
        }

        if(startPosition == null) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }
        if(endPosition == null) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }

        // Get Pawn Promotion Piece
        ChessPiece.PieceType promotionPiece = null;
        if(client.getGame().getBoard().getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN && endPosition.getRow() == 8 && client.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            promotionPiece = promotionPieceType();
        }
        if(client.getGame().getBoard().getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN && endPosition.getRow() == 1 && client.getPlayerColor() == ChessGame.TeamColor.BLACK) {
            promotionPiece = promotionPieceType();
        }

        // Move
        ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
        MakeMove makeMove = new MakeMove(client.getAuthToken(), client.getJoinedGame(), move);
        String json = new Gson().toJson(makeMove);
        try {
            client.getWsFacade().makeMove(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private ChessPiece.PieceType promotionPieceType() {
        ChessPiece.PieceType promotionPiece = null;
        Scanner scanner = new Scanner(System.in);
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\nYour pawn is being promoted! Enter the corresponding number to promote the pawn to that piece:\n" +
                "1 - Queen\n" +
                "2 - Rook\n" +
                "3 - Knight\n" +
                "4 - Bishop\n" +
                ">>> " + SET_TEXT_COLOR_GREEN);
        String line = scanner.nextLine();
        switch(line) {
            case "1" -> promotionPiece = ChessPiece.PieceType.QUEEN;
            case "2" -> promotionPiece = ChessPiece.PieceType.ROOK;
            case "3" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
            case "4" -> promotionPiece = ChessPiece.PieceType.BISHOP;
            default -> {
                System.out.println(SET_TEXT_COLOR_RED + "Error. Enter a correct piece");
                promotionPieceType();
            }
        }
        return promotionPiece;
    }

    private ChessPosition getPosition(String start) {
        int row = Integer.parseInt(start.substring(1));
        int col;
        switch (start.substring(0, 1)) {
            case "a" -> col = 1;
            case "b" -> col = 2;
            case "c" -> col = 3;
            case "d" -> col = 4;
            case "e" -> col = 5;
            case "f" -> col = 6;
            case "g" -> col = 7;
            case "h" -> col = 8;
            default -> {
                System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
                return null;
            }
        }
        return new ChessPosition(row, col);
    }
}
