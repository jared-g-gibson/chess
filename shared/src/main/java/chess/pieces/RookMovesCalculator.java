package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator{


    private final ChessGame.TeamColor pieceColor;

    private final HashSet<ChessMove> moves;

    public RookMovesCalculator(ChessGame.TeamColor c) {
        pieceColor = c;
        moves = new HashSet<>();

    }
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow() + 1;
        int col = myPosition.getColumn();
        // Adds moves that rook can make that go up
        while(row < 9) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            row++;
        }
        // Adds moves that rook can make that go right
        col = myPosition.getColumn() + 1;
        row = myPosition.getRow();
        while(col < 9) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            col++;
        }
        // Adds moves that rook can make that go down
        row = myPosition.getRow() - 1;
        col = myPosition.getColumn();
        while(row > 0) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            row--;
        }
        // Adds moves that queen can make that go left
        row = myPosition.getRow();
        col = myPosition.getColumn() - 1;
        while(col > 0) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            col--;
        }
        return moves;
    }
}
