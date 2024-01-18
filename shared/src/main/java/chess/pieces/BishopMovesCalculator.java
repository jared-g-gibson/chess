package chess.pieces;

import chess.*;

import java.util.*;
public class BishopMovesCalculator {
    /*
     * Function that calculates all possible positions a Bishop can move
     * */
    public static HashSet<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        HashSet<ChessMove> moves = new HashSet<>();
        // Up and to the right
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        row++;
        col++;
        while(row < 9  && row > 0 && col < 9 && col > 0) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of similar color is blocking it
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

        // Down and to the right
        row = myPosition.getRow();
        col = myPosition.getColumn();
        row--;
        col++;
        while(row < 9  && row > 0 && col < 9 && col > 0) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of similar color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            row--;
            col++;
        }

        // Down and to the left
        row = myPosition.getRow();
        col = myPosition.getColumn();
        row--;
        col--;
        while(row < 9  && row > 0 && col < 9 && col > 0) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of similar color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            row--;
            col--;
        }

        // Up and to the left
        row = myPosition.getRow();
        col = myPosition.getColumn();
        row++;
        col--;
        while(row < 9  && row > 0 && col < 9 && col > 0) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of similar color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).getTeamColor() == pieceColor)
                break;
                // Opposing piece can get captured
            else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            row++;
            col--;
        }
        //ChessBoard.getPiece();
        return moves;
    }
}
