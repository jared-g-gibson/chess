package repl;

import chess.*;
import chessClient.ChessClient;
import com.google.gson.Gson;
import exception.ResponseException;
import server.WebSocketFacade;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        while(!result.equals("leave") && !result.equals("resign")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.evalGameplay(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
                if(result.equals("redraw")) {
                    printBoard();
                }
                if(result.startsWith("move")) {
                    makeMove(result);
                }
                if(result.startsWith("leave")) {
                    leave();
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        // printBoard();
    }

    public void printBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.out.println("The boards will be printed here");
        printHeaders();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        printWhiteBoard(board);
        //printInitialWhiteBoard();
        printHeaders();
        System.out.println(SET_BG_COLOR_BLACK);
        printHeadersBackwards();
        printBlackBoard(board);
        //printInitialBlackBoard();
        printHeadersBackwards();
    }


    private void printHeaders() {
        /*for(int x = 0; x < 40; x++) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + " ");
        }*/
        // System.out.println(SET_BG_COLOR_BLACK);
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "     " + SET_TEXT_BOLD);
        for (String header : headers) {
            System.out.print(SET_TEXT_COLOR_BLACK + "  " + header + "  ");
        }
        System.out.println("     " + SET_BG_COLOR_BLACK);
    }

    private void printInitialWhiteBoard() {
        int loopColor = 0;
        for(int x = 1; x < 9; x++) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 0; y < 8; y++) {
                // Gives alternating color cool look
                if(loopColor % 2 == 0)
                    System.out.print(SET_BG_COLOR_WHITE);
                else
                    System.out.print(SET_BG_COLOR_DARK_GREY);

                // Determines which piece to print
                if(x == 8)
                    System.out.print(SET_TEXT_COLOR_RED + "  " + initialRow[y] + "  ");
                else if(x == 7)
                    System.out.print(SET_TEXT_COLOR_RED + "  P  ");
                else if(x == 2)
                    System.out.print(SET_TEXT_COLOR_BLUE + "  P  ");
                else if(x == 1)
                    System.out.print(SET_TEXT_COLOR_BLUE + "  " + initialRow[y] + "  ");
                else
                    System.out.print(SET_TEXT_COLOR_RED + "     ");
                loopColor++;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
    }
    private void printInitialBlackBoard() {
        int loopColor = 0;
        for(int x = 8; x > 0; x--) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 0; y < 8; y++) {
                // Gives alternating color cool look
                if(loopColor % 2 == 0)
                    System.out.print(SET_BG_COLOR_WHITE);
                else
                    System.out.print(SET_BG_COLOR_DARK_GREY);

                // Determines which piece to print
                if(x == 8)
                    System.out.print(SET_TEXT_COLOR_RED + "  " + initialRow[y] + "  ");
                else if(x == 7)
                    System.out.print(SET_TEXT_COLOR_RED + "  P  ");
                else if(x == 2)
                    System.out.print(SET_TEXT_COLOR_BLUE + "  P  ");
                else if(x == 1)
                    System.out.print(SET_TEXT_COLOR_BLUE + "  " + initialRow[y] + "  ");
                else
                    System.out.print(SET_TEXT_COLOR_RED + "     ");
                loopColor++;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
    }

    private void printWhiteBoard(ChessBoard board) {
        int loopColor = 0;
        for(int x = 8; x >= 1; x--) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 1; y <= 8; y++) {
                setColorsAndPrint(board, x, y, loopColor);
                loopColor++;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  SET_TEXT_COLOR_BLACK + "  " + x + "  " +  SET_BG_COLOR_BLACK);
            loopColor++;
        }
    }

    private void printBlackBoard(ChessBoard board) {
        int loopColor = 0;
        for(int x = 1; x <= 8; x++) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  " + x + "  " + SET_BG_COLOR_WHITE);
            for(int y = 1; y <= 8; y++) {
                setColorsAndPrint(board, x, y, loopColor);
                loopColor++;
            }
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

        System.out.print(SET_BG_COLOR_LIGHT_GREY + "     " + SET_TEXT_BOLD);
        for (int x = 7; x >= 0; x--) {
            System.out.print(SET_TEXT_COLOR_BLACK + "  " + headers[x] + "  ");
        }
        System.out.println("     " + SET_BG_COLOR_BLACK);
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

    public void makeMove(String command) throws ResponseException {
        command = command.toLowerCase();
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
        if(start.charAt(0) != 'a' || start.charAt(0) != 'b' || start.charAt(0) != 'c' || start.charAt(0) != 'd' ||
                start.charAt(0) != 'e' || start.charAt(0) != 'f' || start.charAt(0) != 'g' || start.charAt(0) != 'h') {
                System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
                return;
        }
        if(end.charAt(0) != 'a' || end.charAt(0) != 'b' || end.charAt(0) != 'c' || end.charAt(0) != 'd' ||
                end.charAt(0) != 'e' || end.charAt(0) != 'f' || end.charAt(0) != 'g' || end.charAt(0) != 'h') {
            System.out.println(SET_TEXT_COLOR_RED + "Error: follow the format given to make move. See help for more details.");
            return;
        }

        // Get start and end positions from strings
        // Example a2a3 -> StartPosition: (1,2) EndPosition: (1, 3)
        ChessPosition startPosition = getPosition(start);
        ChessPosition endPosition = getPosition(end);
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
        if(client.getGame().getBoard().getPiece(endPosition).getPieceType() == ChessPiece.PieceType.PAWN && endPosition.getRow() == 8 && client.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            promotionPiece = promotionPieceType();
        }
        if(client.getGame().getBoard().getPiece(endPosition).getPieceType() == ChessPiece.PieceType.PAWN && endPosition.getRow() == 1 && client.getPlayerColor() == ChessGame.TeamColor.BLACK) {
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
