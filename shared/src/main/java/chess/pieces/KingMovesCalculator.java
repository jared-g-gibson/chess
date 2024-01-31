package chess.pieces;

import chess.*;
import java.util.*;
public class KingMovesCalculator implements PieceMovesCalculator {

    private final ChessGame.TeamColor pieceColor;
    private final HashSet<ChessMove> moves;
    public KingMovesCalculator(ChessGame.TeamColor c) {
        pieceColor = c;
        moves = new HashSet<>();
    }
    /*
     * Function that calculates all possible positions a King can move from current position
     * */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Check if King can move up
        ChessMove move = kingHelper(board, myPosition, row + 1, col, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move down
        move = kingHelper(board, myPosition, row - 1, col, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move left
        move = kingHelper(board, myPosition, row, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move right
        move = kingHelper(board, myPosition, row, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move up and to the right
        move = kingHelper(board, myPosition, row + 1, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move up and to the left
        move = kingHelper(board, myPosition, row + 1, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move down and to the right
        move = kingHelper(board, myPosition, row - 1, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move down and to the left
        move = kingHelper(board, myPosition, row - 1, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        return moves;
    }
    public static ChessMove kingHelper(ChessBoard board, ChessPosition myPosition, int row, int col, ChessGame.TeamColor pieceColor) {
        if(row > 0 && row < 9 && col > 0 && col < 9 && board.getPiece(new ChessPosition(row, col)) == null)
            return new ChessMove(myPosition, new ChessPosition(row, col), null);
        else if(row > 0 && row < 9 && col > 0 && col < 9 && board.getPiece(new ChessPosition(row, col)).getTeamColor() != pieceColor)
            return new ChessMove(myPosition, new ChessPosition(row, col), null);
        else
            return null;
    }
}
