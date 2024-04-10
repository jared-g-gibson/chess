package repl;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import chessClient.ChessClient;
import com.google.gson.Gson;
import exception.ResponseException;
import server.WebSocketFacade;
import webSocketMessages.userCommands.JoinPlayer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayUI implements GameHandler{
    private final String[] headers;
    private final ChessClient client;
    private static Random rand = new Random();
    private final String[] initialRow;

    private String authToken;

    private WebSocketFacade wsFacade;

    // Red is White Team
    // Blue is Black Team

    public GameplayUI(ChessClient client) {
        this.client = client;
        this.authToken = client.getAuthToken();
        headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        initialRow = new String[]{"R", "N", "B", "Q", "K", "B", "N", "R"};
        try {
            this.wsFacade = new WebSocketFacade(client.getURL());
            this.joinPlayer();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        printBoard();
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
            wsFacade.joinPlayer(json);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


    @Override
    public void updateGame(int game) {

    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
