package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public interface PieceMovesCalculator {

    HashSet<ChessMove> moves = new HashSet<>();
    HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
