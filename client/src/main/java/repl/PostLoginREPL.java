package repl;

import chessClient.ChessClient;

import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class PostLoginREPL {
    private final ChessClient client;
    public PostLoginREPL(ChessClient myClient) {
        this.client = myClient;
    }
    public String run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
                if(result.startsWith("logged out"))
                    break;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public void printPrompt() {
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\n[LOGGED_IN] >>> " + SET_TEXT_COLOR_GREEN);
    }
}
