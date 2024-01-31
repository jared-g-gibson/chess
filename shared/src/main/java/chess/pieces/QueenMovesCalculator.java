package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {

    private final ChessGame.TeamColor pieceColor;

    private final HashSet<ChessMove> moves;

    public QueenMovesCalculator(ChessGame.TeamColor c) {
        pieceColor = c;
        moves = new HashSet<>();

    }


    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        /*
        * Since the Queen takes on both the bishop and rooks moves, this code makes sense
        * */
        BishopMovesCalculator bishop = new BishopMovesCalculator(pieceColor);
        RookMovesCalculator rook = new RookMovesCalculator(pieceColor);
        moves.addAll(bishop.pieceMoves(board, myPosition));
        moves.addAll(rook.pieceMoves(board, myPosition));
        return moves;
    }
}
