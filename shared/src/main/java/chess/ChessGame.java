package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    // TO DO: Make sure the king gets out of check

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        // There is no piece at this position
        if (piece == null)
            return null;

        // Get the possible moves for the given piece
        Collection<ChessMove> moves = new HashSet<>();
        moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        // Iterate through moves, and simulate the move.
        // If the team's king goes into check from the move, remove that move.
        for(ChessMove move : moves) {
            // Make a copy of the chess board that we can revert back to
            //ChessBoard boardCopy = new ChessBoard(board);

            // Make the move
            ChessPiece removedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(startPosition);

            // If king is not in check, add the move to valid moves
            if(!isInCheck(piece.getTeamColor()))
                validMoves.add(move);

            // Reset board back to initial state
            board.addPiece(startPosition, piece);
            board.addPiece(move.getEndPosition(), removedPiece);
        }

        // Return the valid moves.
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // throw new RuntimeException("Not implemented");

        //System.out.println(board.toString());

        // It's not the piece's turn
        if(board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn())
            throw new InvalidMoveException();

        // The piece's final position is not in its moveset
        Collection<ChessMove> validMoves = new HashSet<>();
        validMoves = validMoves(move.getStartPosition());

        for(ChessMove m : validMoves) {
            // Execute the move
            if(move.equals(m)) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
                if(board.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.BLACK)
                    setTeamTurn(TeamColor.WHITE);
                else
                    setTeamTurn(TeamColor.BLACK);
                return;
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        // ChessBoard boardCopy = new ChessBoard(getBoard());
        // White Team
        if(teamColor == TeamColor.WHITE) {
            // Find the White King
            ChessPosition kingPos = board.findPiece(new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING));
            // Calculate all possible moves for Black to see if it can capture White King
            return canCaptureKing(kingPos, TeamColor.WHITE);
        }
        // Black Team
        else {
            // Find the Black King
            ChessPosition kingPos = board.findPiece(new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING));
            // Calculate all possible moves for White to see if it can capture Black King
            return canCaptureKing(kingPos, TeamColor.BLACK);
        }
    }
    public boolean canCaptureKing(ChessPosition kingPos, TeamColor color) {
        // Returns whether white piece can capture black king
        if(color == TeamColor.BLACK) {
            // Loop through all the pieces
            for(int x = 1; x < 9; x++) {
                for(int y = 1; y < 9; y++) {
                    Collection<ChessMove> moves = new HashSet<>();
                    ChessPiece piece = board.getPiece(new ChessPosition(x, y));
                    // For each piece of a different color, check and see if king position
                    // is one of the possible piece moves
                    if(piece != null && color != piece.getTeamColor()) {
                        moves = piece.pieceMoves(board, new ChessPosition(x, y));
                        if(piece.findMove(kingPos, moves))
                            return true;
                    }
                }
            }
        }

        // Returns whether black piece can capture white king
        else {
            // Loop through all the pieces
            for(int x = 1; x < 9; x++) {
                for(int y = 1; y < 9; y++) {
                    Collection<ChessMove> moves = new HashSet<>();
                    ChessPiece piece = board.getPiece(new ChessPosition(x, y));
                    // For each piece of a different color, check and see if king position
                    // is one of the possible piece moves
                    if(piece != null && color != piece.getTeamColor()) {
                        moves = piece.pieceMoves(board, new ChessPosition(x, y));
                        if(piece.findMove(kingPos, moves))
                            return true;
                    }
                }
            }
        }


        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
