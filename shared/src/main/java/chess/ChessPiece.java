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
        else if(type == PieceType.KNIGHT)
            return calculateKnightMoves(board, myPosition);
        else if(type == PieceType.PAWN)
            return calculatePawnMoves(board, myPosition);
        else if(type == PieceType.QUEEN)
            return calculateQueenMoves(board, myPosition);
        else
            return calculateRookMoves(board, myPosition);
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

    /*
    * Function that returns possible moves for a knight
    * */

    public HashSet<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Up and left combos
        ChessMove move = knightHelper(board, myPosition, row + 1, col - 2);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row + 2, col - 1);
        if(move != null)
            moves.add(move);

        // Up and right combos
        move = knightHelper(board, myPosition, row + 1, col + 2);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row + 2, col + 1);
        if(move != null)
            moves.add(move);

        // Down and left combos
        move = knightHelper(board, myPosition, row - 1, col - 2);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row - 2, col - 1);
        if(move != null)
            moves.add(move);

        // Down and right combos
        move = knightHelper(board, myPosition, row - 1, col + 2);
        if(move != null)
            moves.add(move);
        move = knightHelper(board, myPosition, row - 2, col + 1);
        if(move != null)
            moves.add(move);

        return moves;
    }

    public ChessMove knightHelper(ChessBoard board, ChessPosition myPosition, int row, int col) {
        if(row > 0 && row < 9 && col > 0 && col < 9 && board.getPiece(new ChessPosition(row, col)) == null)
            return new ChessMove(myPosition, new ChessPosition(row, col), null);
        else if(row > 0 && row < 9 && col > 0 && col < 9 && board.getPiece(new ChessPosition(row, col)).pieceColor != pieceColor)
            return new ChessMove(myPosition, new ChessPosition(row, col), null);
        else
            return null;
    }

    /*
    * Function that calculates all possible pawn moves and promotions
    *  */
    public HashSet<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // White Moves
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            if(row + 1 < 9 && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                // Promotion moving normally
                if(row + 1 == 8) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), PieceType.ROOK));
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
            if(row + 1 < 9 && col + 1 < 9 && board.getPiece(new ChessPosition(row + 1, col + 1)) != null && board.getPiece(new ChessPosition(row + 1, col + 1)).pieceColor != pieceColor) {
                // Capturing piece up and to the right (promotion)
                if(row + 1 == 8) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), PieceType.ROOK));
                }
                // Capturing piece up and to the right (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), null));
            }
            if(row + 1 < 9 && col - 1 > 0 && board.getPiece(new ChessPosition(row + 1, col - 1)) != null && board.getPiece(new ChessPosition(row + 1, col - 1)).pieceColor != pieceColor) {
                // Capturing piece down and to the left (promotion)
                if(row + 1 == 8) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), PieceType.ROOK));
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
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), PieceType.ROOK));
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
            if(row - 1 > 0 && col + 1 < 9 && board.getPiece(new ChessPosition(row - 1, col + 1)) != null && board.getPiece(new ChessPosition(row - 1, col + 1)).pieceColor != pieceColor) {
                // Capturing piece down and to the right (promotion)
                if(row - 1 == 1) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), PieceType.ROOK));
                }
                // Capturing piece down and to the right (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), null));
            }
            if(row - 1 > 0 && col - 1 > 0 && board.getPiece(new ChessPosition(row - 1, col - 1)) != null && board.getPiece(new ChessPosition(row - 1, col - 1)).pieceColor != pieceColor) {
                // Capturing piece down and to the left (promotion)
                if(row - 1 == 1) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), PieceType.ROOK));
                }
                // Capturing piece down and to the left (not promotion)
                else
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), null));
            }
        }
        return moves;
    }

    public HashSet<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow() + 1;
        int col = myPosition.getColumn();
        // Adds moves that queen can make that go up
        while(row < 9) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
            // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
        // Adds moves that queen can make that go right
        col = myPosition.getColumn() + 1;
        row = myPosition.getRow();
        while(col < 9) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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

    public HashSet<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow() + 1;
        int col = myPosition.getColumn();
        // Adds moves that rook can make that go up
        while(row < 9) {
            // Checks if there is no piece there
            if(board.getPiece(new ChessPosition(row, col)) == null)
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                // Checks to see if piece of same color is blocking it
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
            else if(board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor)
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
