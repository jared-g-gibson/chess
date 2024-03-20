import chess.*;
import repl.PreLoginREPL;
public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        PreLoginREPL repl = new PreLoginREPL();
        repl.run();
    }
}