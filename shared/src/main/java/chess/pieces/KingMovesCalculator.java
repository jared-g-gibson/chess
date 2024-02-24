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

        // Create instance of Knight Moves Calculator to get
        // access to isValidMove method
        KnightMovesCalculator calculator = new KnightMovesCalculator(pieceColor);

        // Check if King can move up
        ChessMove move = calculator.isValidMove(board, myPosition, row + 1, col, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move down
        move = calculator.isValidMove(board, myPosition, row - 1, col, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move left
        move = calculator.isValidMove(board, myPosition, row, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move right
        move = calculator.isValidMove(board, myPosition, row, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move up and to the right
        move = calculator.isValidMove(board, myPosition, row + 1, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move up and to the left
        move = calculator.isValidMove(board, myPosition, row + 1, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move down and to the right
        move = calculator.isValidMove(board, myPosition, row - 1, col + 1, pieceColor);
        if(move != null)
            moves.add(move);

        // Check if King can move down and to the left
        move = calculator.isValidMove(board, myPosition, row - 1, col - 1, pieceColor);
        if(move != null)
            moves.add(move);

        return moves;
    }
}
