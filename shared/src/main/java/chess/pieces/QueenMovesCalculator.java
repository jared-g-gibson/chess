package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class QueenMovesCalculator {
    public static HashSet<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow() + 1;
        int col = myPosition.getColumn();
        // Adds moves that queen can make that go up
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
        // Adds moves that queen can make that go up and to the right
        row = myPosition.getRow() + 1;
        col = myPosition.getColumn() + 1;
        while(row < 9 && col < 9) {
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
            col++;
        }
        // Adds moves that queen can make that go right
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
        // Adds moves that queen can make that go down and right
        row = myPosition.getRow() - 1;
        col = myPosition.getColumn() + 1;
        while(col < 9 && row > 0) {
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
            row--;
        }
        // Adds moves that queen can make that go down
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
        // Adds moves that queen can make that go left and down
        row = myPosition.getRow() - 1;
        col = myPosition.getColumn() - 1;
        while(row > 0 && col > 0) {
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
        // Adds moves that queen can make that go left and up
        row = myPosition.getRow() + 1;
        col = myPosition.getColumn() - 1;
        while(col > 0 && row < 9) {
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
            row++;
        }
        return moves;
    }
}
