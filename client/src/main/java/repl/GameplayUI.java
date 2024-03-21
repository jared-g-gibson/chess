package repl;

import chessClient.ChessClient;
import ui.EscapeSequences;

import static ui.EscapeSequences.*;

public class GameplayUI {
    private final String[] headers;
    private final ChessClient client;

    public GameplayUI(ChessClient client) {
        this.client = client;
        headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
    }

    public void run() {
        printBoard();
    }

    public void printBoard() {
        System.out.println("The board will be printed here");
        printHeaders();
        for(int x = 1; x < 9; x++) {
            printInitialRows(x);
        }
        printHeaders();

        System.out.println("\n\nThe board backwards will be printed here");
        printHeadersBackwards();
        for(int x = 8; x > 0; x--) {
            printInitialRows(x);
        }
        printHeadersBackwards();
    }

    private void printHeaders() {
        System.out.print("   " + SET_TEXT_BOLD);
        for (String header : headers) {
            System.out.print(header + "   ");
        }
        System.out.println();
    }

    private void printHeadersBackwards() {
        System.out.print("   " + SET_TEXT_BOLD);
        for (int x = 7; x >= 0; x--) {
            System.out.print(headers[x] + "   ");
        }
        System.out.println();
    }

    private void printInitialRows(int row) {
        switch (row) {
            case 8 -> {
                System.out.print("8 " + SET_TEXT_BOLD);
                System.out.println(BLACK_ROOK + "|" + BLACK_KNIGHT + "|" + BLACK_BISHOP + "|" + BLACK_KING + "|"  + BLACK_QUEEN + "|" + BLACK_BISHOP + "|" + BLACK_KNIGHT + "|" + BLACK_ROOK + "| 8");
                //System.out.println("R   N   B   K   Q   B   N   R    1");
            }
            case 7 -> {
                System.out.print("7 " + SET_TEXT_BOLD);
                System.out.println(BLACK_PAWN + "|" + BLACK_PAWN + "|" + BLACK_PAWN + "|" + BLACK_PAWN + "|"  + BLACK_PAWN + "|" + BLACK_PAWN + "|" + BLACK_PAWN + "|" + BLACK_PAWN + "| 2");
            }
            case 2 -> {
                System.out.print("2 " + SET_TEXT_BOLD);
                System.out.println(WHITE_PAWN + "|" + WHITE_PAWN + "|" + WHITE_PAWN + "|" + WHITE_PAWN + "|"  + WHITE_PAWN + "|" + WHITE_PAWN + "|" + WHITE_PAWN + "|" + WHITE_PAWN + "| 2");
                //System.out.println("P   P   P   P   P   P   P   P    2");
            }
            case 1 -> {
                System.out.print("1 " + SET_TEXT_BOLD);
                System.out.println(WHITE_ROOK + "|" + WHITE_KNIGHT + "|" + WHITE_BISHOP + "|" + WHITE_KING + "|"  + WHITE_QUEEN + "|" + WHITE_BISHOP + "|" + WHITE_KNIGHT + "|" + WHITE_ROOK + "| 1");

                //System.out.println("R   N   B   K   Q   B   N   R    1");
            }
            default -> System.out.println(SET_TEXT_BOLD + row + "    |   |   |   |   |   |   |   | " + row);
        }

        /*if(row == 1) {
            System.out.print("\n1    " + SET_TEXT_BOLD);
            System.out.println("R   N   B   K   Q   B   N   R    1");
        }
        if(row == 2) {
            System.out.print("2    " + SET_TEXT_BOLD);
            System.out.println("P   P   P   P   P   P   P   P    2");
        }
        if*/
    }
}
