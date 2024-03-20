package chess.model.piece;

import chess.model.Position;

public class Rook extends Piece {

    public Rook(PieceType pieceType) {
        super(pieceType);
    }

    @Override
    public boolean canMove(Position source, Position target) {
        return false;
    }
}
