package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(type == PieceType.BISHOP)
            return calculateBishopMoves(board, myPosition);
        else if(type == PieceType.KING)
            return calculateKingMoves(board, myPosition);
        return new HashSet<ChessMove>();
        //throw new RuntimeException("Not implemented");
    }

    /*
    * Function that calculates all possible positions a Bishop can move
    * */
    public HashSet<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition myPosition) {
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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

    /*
     * Function that calculates all possible positions a King can move from current position
     * */
    public HashSet<ChessMove> calculateKingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Check if King can move up
        if(row + 1 < 9 && board.getPiece(new ChessPosition(row+1, col)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col), null));
        else if(row + 1 < 9 && board.getPiece(new ChessPosition(row+1, col)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col), null));

        // Check if King can move down
        if(row - 1 > 0 && board.getPiece(new ChessPosition(row-1, col)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col), null));
        else if(row - 1 > 0 && board.getPiece(new ChessPosition(row-1, col)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col), null));

        // Check if King can move left
        if(col - 1 > 0 && board.getPiece(new ChessPosition(row, col-1)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col-1), null));
        else if(col - 1 > 0 && board.getPiece(new ChessPosition(row, col-1)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col-1), null));

        // Check if King can move right
        if(col + 1 < 9 && board.getPiece(new ChessPosition(row, col+1)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col+1), null));
        else if(col + 1 < 9 && board.getPiece(new ChessPosition(row, col+1)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row, col+1), null));

        // Check if King can move up and to the right
        if(col + 1 < 9 && row + 1 < 9 && board.getPiece(new ChessPosition(row+1, col+1)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col+1), null));
        else if(col + 1 < 9 && row + 1 < 9 && board.getPiece(new ChessPosition(row+1, col+1)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col+1), null));

        // Check if King can move up and to the left
        if(col - 1 > 0 && row + 1 < 9 && board.getPiece(new ChessPosition(row+1, col-1)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col-1), null));
        else if(col - 1 > 0 && row + 1 < 9 && board.getPiece(new ChessPosition(row+1, col-1)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col-1), null));

        // Check if King can move down and to the right
        if(col + 1 < 9 && row - 1 > 0 && board.getPiece(new ChessPosition(row-1, col+1)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col+1), null));
        else if(col + 1 < 9 && row - 1 > 0 && board.getPiece(new ChessPosition(row-1, col+1)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col+1), null));

        // Check if King can move down and to the left
        if(col - 1 > 0 && row - 1 > 0 && board.getPiece(new ChessPosition(row-1, col-1)) == null)
            moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col-1), null));
        else if(col - 1 > 0 && row - 1 > 0 && board.getPiece(new ChessPosition(row-1, col-1)).pieceColor != pieceColor)
            moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col-1), null));

        return moves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
