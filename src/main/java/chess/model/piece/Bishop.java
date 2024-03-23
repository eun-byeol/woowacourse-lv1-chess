package chess.model.piece;

import static chess.model.Direction.DOWN_LEFT;
import static chess.model.Direction.DOWN_RIGHT;
import static chess.model.Direction.UP_LEFT;
import static chess.model.Direction.UP_RIGHT;

import chess.model.Direction;
import chess.model.Position;
import chess.model.material.Color;
import chess.model.material.Type;

public class Bishop extends Piece {

    public Bishop(Type type, Color color) {
        super(type, color);
    }

    @Override
    public boolean canMove(Position source, Position target) {
        Direction direction = Direction.findDirection(source, target);
        return direction == UP_LEFT || direction == DOWN_LEFT || direction == UP_RIGHT || direction == DOWN_RIGHT;
    }
}
