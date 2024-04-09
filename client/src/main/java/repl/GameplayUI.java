package repl;

import chessClient.ChessClient;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class GameplayUI {
    private final String[] headers;
    private final ChessClient client;
    private static Random rand = new Random();
    private final String[] initialRow;



    public GameplayUI(ChessClient client) {
        this.client = client;
        headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        initialRow = new String[]{"R", "N", "B", "Q", "K", "B", "N", "R"};
    }

    public void run() {
        printBoard();
    }

    public void printBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.out.println("The boards will be printed here");
        printHeaders();
        printInitialWhiteBoard();
        printHeaders();
        System.out.println(SET_BG_COLOR_BLACK);
        printHeadersBackwards();
        printInitialBlackBoard();
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

    private void printHeadersBackwards() {

        System.out.print(SET_BG_COLOR_LIGHT_GREY + "     " + SET_TEXT_BOLD);
        for (int x = 7; x >= 0; x--) {
            System.out.print(SET_TEXT_COLOR_BLACK + "  " + headers[x] + "  ");
        }
        System.out.println("     " + SET_BG_COLOR_BLACK);
    }

}
