package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KnightMovesCalculator {
    /*
     * Function that returns possible moves for a knight
     * */

    public static HashSet<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Up and left combos
        ChessMove move = knightHelper(board, myPosition, row + 1, col - 2, pieceColor);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row + 2, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Up and right combos
        move = knightHelper(board, myPosition, row + 1, col + 2, pieceColor);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row + 2, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Down and left combos
        move = knightHelper(board, myPosition, row - 1, col - 2, pieceColor);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row - 2, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Down and right combos
        move = knightHelper(board, myPosition, row - 1, col + 2, pieceColor);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row - 2, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        return moves;
    }

    public static ChessMove knightHelper(ChessBoard board, ChessPosition myPosition, int row, int col, ChessGame.TeamColor pieceColor) {
        if(row > 0 && row < 9 && col > 0 && col < 9 && board.getPiece(new ChessPosition(row, col)) == null)
            return new ChessMove(myPosition, new ChessPosition(row, col), null);
        else if(row > 0 && row < 9 && col > 0 && col < 9 && board.getPiece(new ChessPosition(row, col)).getTeamColor() != pieceColor)
            return new ChessMove(myPosition, new ChessPosition(row, col), null);
        else
            return null;
    }
}
