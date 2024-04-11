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

    private boolean gameOver;

    public ChessGame() {
        turn = TeamColor.WHITE;
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver() {
        gameOver = true;
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
        // There is no piece at this position, so the valid moves is null
        if (piece == null)
            return null;

        // Get the possible moves for the given piece
        Collection<ChessMove> moves;
        moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        // Iterate through moves, and simulate the move.
        // If the team's king goes into check from the move,
        // don't add move to valid moves.
        for(ChessMove move : moves) {
            // Simulate the move
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
        if(gameOver)
            throw new InvalidMoveException();
        // It's not the piece's turn, so throw InvalidMoveException
        if(board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn())
            throw new InvalidMoveException();

        // The piece's final position is not in its move set
        Collection<ChessMove> validMoves = new HashSet<>();
        validMoves = validMoves(move.getStartPosition());

        for(ChessMove m : validMoves) {
            // Execute the move
            if(move.equals(m)) {
                // If there is a promotionPiece, add that piece to the board,
                // else, add the regular piece
                // Remove the old piece from the board
                if (move.getPromotionPiece() == null) {
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                }
                else {
                    board.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), move.getPromotionPiece()));
                }
                board.removePiece(move.getStartPosition());

                // Change turn to other team since valid move was made
                if(board.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.BLACK)
                    setTeamTurn(TeamColor.WHITE);
                else
                    setTeamTurn(TeamColor.BLACK);
                return;
            }
        }

        // Invalid move was passed to method, so throw InvalidMoveException
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
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
        return kingInDanger(kingPos, color);
    }

    public boolean kingInDanger(ChessPosition kingPos, TeamColor color) {
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
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // According to our definition, a team is in checkmate if they are
        // unable to move and are in check
        gameOver = true;
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // Iterate through the whole board
        // Call valid moves on each teamColor piece
        // If validMoves == null for every piece, return true
        for(int x = 1; x < 9; x++) {
            for(int y = 1; y < 9; y++) {
                ChessPiece piece = board.getPiece(new ChessPosition(x, y));
                if(piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(x, y));
                    if(!moves.isEmpty())
                        return false;
                }
            }
        }
        return true;
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
