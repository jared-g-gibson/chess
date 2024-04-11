package repl;

import chess.ChessBoard;
import chess.ChessGame;
import chessClient.ChessClient;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLoginREPL implements GameHandler {
    private final ChessClient client;
    public PreLoginREPL() {
        client = new ChessClient("http://localhost:8080", this);
    }
    public void run() {
        System.out.println("Welcome to 240 Chess. Type help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                if(result.startsWith("ERROR"))
                    System.out.println(SET_TEXT_COLOR_RED + result);
                else
                    System.out.println(SET_TEXT_COLOR_BLUE + result);
                if(result.startsWith("Logged in")) {
                    PostLoginREPL post = new PostLoginREPL(client);
                    result = post.run();
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void printPrompt() {
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\n[LOGGED_OUT] >>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void updateGame(ChessGame game) {
        if(game.getBoard() == null) {
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            game.setBoard(board);
        }
        client.setGame(game);
        client.redrawBoard();
        System.out.println();
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\n[PLAYING_GAME] >>> " + SET_TEXT_COLOR_GREEN);

    }

    @Override
    public void printMessage(String message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
        System.out.println();
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\n[PLAYING_GAME] >>> " + SET_TEXT_COLOR_GREEN);
    }
}
