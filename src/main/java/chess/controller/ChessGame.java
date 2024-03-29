package chess.controller;

import static chess.model.material.Color.BLACK;
import static chess.model.material.Color.WHITE;

import chess.dto.BoardDto;
import chess.dto.ColorScoreDto;
import chess.dto.WinnerDto;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.board.InitialBoardFactory;
import chess.model.game.GameStatus;
import chess.model.material.Color;
import chess.model.outcome.ScoreCalculator;
import chess.model.outcome.Winner;
import chess.model.position.Position;
import chess.view.InputView;
import chess.view.OutputView;
import java.util.List;
import java.util.function.Supplier;

public final class ChessGame {

    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;

    private final InputView inputView;
    private final OutputView outputView;

    public ChessGame(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        inputView.printGameIntro();
        GameStatus gameStatus = new GameStatus(
            this::executeStart,
            this::executeMove,
            this::executeStatus,
            this::executeEnd
        );
        BoardFactory boardFactory = new InitialBoardFactory();
        Board board = boardFactory.generate();
        while (gameStatus.isRunning()) {
            gameStatus = executeGame(board, gameStatus);
        }
    }

    private GameStatus executeGame(Board board, GameStatus gameStatus) {
        return retryOnException(() -> executeCommand(board, gameStatus));
    }

    private GameStatus executeCommand(Board board, GameStatus gameStatus) {
        List<String> commands = inputView.askGameCommands();
        return gameStatus.action(commands, board);
    }

    private void executeStart(Board board) {
        BoardDto boardDto = BoardDto.from(board);
        outputView.printChessBoard(boardDto);
    }

    private boolean executeMove(List<String> commands, Board board) {
        Position source = Position.from(commands.get(SOURCE_INDEX));
        Position target = Position.from(commands.get(TARGET_INDEX));
        boolean isKingCaught = board.move(source, target);
        BoardDto boardDto = BoardDto.from(board);
        outputView.printChessBoard(boardDto);
        if (isKingCaught) {
            Color color = board.lastTurn();
            WinnerDto result = WinnerDto.from(color);
            outputView.printGameResult(result);
        }
        return isKingCaught;
    }

    private void executeStatus(Board board) {
        ScoreCalculator scoreCalculator = board.calculateScore();
        ColorScoreDto whiteScore = scoreCalculator.calculate(WHITE);
        ColorScoreDto blackScore = scoreCalculator.calculate(BLACK);
        outputView.printScoreStatus(whiteScore, blackScore);
    }

    private void executeEnd(Board board) {
        ScoreCalculator scoreCalculator = board.calculateScore();
        ColorScoreDto whiteScore = scoreCalculator.calculate(WHITE);
        ColorScoreDto blackScore = scoreCalculator.calculate(BLACK);
        outputView.printScoreStatus(whiteScore, blackScore);
        WinnerDto winnerDto = Winner.of(whiteScore, blackScore);
        outputView.printGameResult(winnerDto);
    }

    private <T> T retryOnException(Supplier<T> operation) {
        try {
            return operation.get();
        } catch (Exception e) {
            outputView.printException(e);
            return retryOnException(operation);
        }
    }
}
