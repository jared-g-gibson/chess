package chess.pieces;

import chess.*;

import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {

    private final ChessGame.TeamColor pieceColor;

    HashSet<ChessMove> moves;

    public PawnMovesCalculator(ChessGame.TeamColor c) {
        moves = new HashSet<>();
        pieceColor = c;
    }
    /*
     * Function that calculates all possible pawn moves and promotions
     *  */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // White Moves
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            if(row + 1 < 9 && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                // Promotion moving normally
                if(row + 1 == 8) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), ChessPiece.PieceType.ROOK));
                }
                else {
                    // Normal pawn move
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                    // Pawn moving forward 2 when not blocked when it is at starting position
                    if(row == 2 && board.getPiece(new ChessPosition(row + 2, col)) == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col), null));
                    }
                }
            }
            if(row + 1 < 9 && col + 1 < 9 && board.getPiece(new ChessPosition(row + 1, col + 1)) != null && board.getPiece(new ChessPosition(row + 1, col + 1)).getTeamColor() != pieceColor) {
                // Capturing piece up and to the right (promotion)
                if(row + 1 == 8) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.ROOK));
                }
                // Capturing piece up and to the right (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), null));
            }
            if(row + 1 < 9 && col - 1 > 0 && board.getPiece(new ChessPosition(row + 1, col - 1)) != null && board.getPiece(new ChessPosition(row + 1, col - 1)).getTeamColor() != pieceColor) {
                // Capturing piece down and to the left (promotion)
                if(row + 1 == 8) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.ROOK));
                }
                // Capturing piece down and to the left (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), null));
            }
        }
        // Black Moves
        else {
            if(row - 1 > 0 && board.getPiece(new ChessPosition(row - 1, col)) == null) {
                // Promotion moving normally
                if(row - 1 == 1) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), ChessPiece.PieceType.ROOK));
                }
                else {
                    // Normal pawn move
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                    // Pawn moving forward 2 when not blocked when it is at starting position
                    if(row == 7 && board.getPiece(new ChessPosition(row - 2, col)) == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col), null));
                    }
                }
            }
            if(row - 1 > 0 && col + 1 < 9 && board.getPiece(new ChessPosition(row - 1, col + 1)) != null && board.getPiece(new ChessPosition(row - 1, col + 1)).getTeamColor() != pieceColor) {
                // Capturing piece down and to the right (promotion)
                if(row - 1 == 1) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.ROOK));
                }
                // Capturing piece down and to the right (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), null));
            }
            if(row - 1 > 0 && col - 1 > 0 && board.getPiece(new ChessPosition(row - 1, col - 1)) != null && board.getPiece(new ChessPosition(row - 1, col - 1)).getTeamColor() != pieceColor) {
                // Capturing piece down and to the left (promotion)
                if(row - 1 == 1) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.ROOK));
                }
                // Capturing piece down and to the left (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), null));
            }
        }
        return moves;
    }
}
