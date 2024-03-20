package chess.model.piece;

import static chess.model.piece.PieceType.BLACK_QUEEN;

import chess.model.Position;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QueenTest {

    @DisplayName("Queen이 상하좌우 대각선 이동이면 canMove true를 반환하고, 아니면 false를 반환한다")
    @ParameterizedTest
    @MethodSource("provideSourceAndTargetWithExpected")
    void queenCanMove(Position source, Position target, boolean expected) {
        Piece piece = new Queen(BLACK_QUEEN);
        boolean canMove = piece.canMove(source, target);
        Assertions.assertThat(canMove).isEqualTo(expected);
    }

    public static Stream<Arguments> provideSourceAndTargetWithExpected() {
        return Stream.of(
                Arguments.of(new Position(3, 3), new Position(3, 7), true),
                Arguments.of(new Position(3, 3), new Position(7, 7), true),
                Arguments.of(new Position(3, 3), new Position(5, 4), false)
        );
    }
}
