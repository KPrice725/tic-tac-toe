package com.boxnotfound.tictactoe.game;

import com.boxnotfound.tictactoe.model.TicTacToeBoard;
import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileColor;
import com.boxnotfound.tictactoe.model.TileStatus;
import com.boxnotfound.tictactoe.model.wincondition.WinCondition;
import com.boxnotfound.tictactoe.model.wincondition.WinConditionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;

public class GamePresenter implements GameContract.Presenter {

    private GameContract.View gameView;
    private TicTacToeTile[][] gameBoard;
    private static final int GAME_BOARD_MAX_ROW_SIZE = 8;
    private static final int GAME_BOARD_MIN_ROW_SIZE = 4;
    private static int gameBoardRowSize = 4;
    private static int gameBoardSize = gameBoardRowSize * gameBoardRowSize;
    private static int moveCount;
    private static TileStatus currentPlayer;
    private static boolean gameOver;
    private static boolean firstLaunch = true;
    private static int rowOfLastMove, columnOfLastMove;

    private static SparseArrayCompat<List<WinCondition>> winConditionMap;

    public GamePresenter(@NonNull final GameContract.View gameView) {
        this.gameView = gameView;
        gameView.setPresenter(this);
    }

    @Override
    public void start() {
        // since this is automatically called by the view upon creation, pass in false
        // to prevent game refreshes in instances of screen orientation change or
        // restarting of application after it has been paused by the OS
        launchNewTicTacToeGame(false);
    }

    @Override
    public void launchNewTicTacToeGame(final boolean userRequested) {
        if (userRequested || firstLaunch) {
            gameBoard = TicTacToeBoard.setupTicTacToeBoard(gameBoardRowSize, true);
            firstLaunch = false;
            gameOver = false;
            rowOfLastMove = columnOfLastMove = -1;
            moveCount = 0;
            currentPlayer = TileStatus.PLAYER_X;
            setupWinConditions(gameBoard);
        } else {
            gameBoard = TicTacToeBoard.setupTicTacToeBoard(gameBoardRowSize, false);
        }
        if (!gameOver) {
            gameView.displayPlayerTurn(currentPlayer);
        }
        setupTileListForView();
    }

    private void setupWinConditions(@NonNull final TicTacToeTile[][] gameBoard) {

        if (winConditionMap != null) {
            winConditionMap.clear();
        }

        winConditionMap = WinConditionUtils.generateWinConditionsFromTicTacToeBoard(gameBoard);
    }

    private void setupTileListForView() {
        List<TicTacToeTile> tiles = new ArrayList<>();
        for (TicTacToeTile[] tileRow : gameBoard) {
            tiles.addAll(Arrays.asList(tileRow));
        }
        gameView.displayNewTicTacToeGame(tiles, gameBoardRowSize);
    }

    @Override
    public void setPlayerMove(final int gridIndex) {
        if (!gameOver) {
            int row = gridIndex / gameBoardRowSize;
            int col = gridIndex % gameBoardRowSize;
            TicTacToeTile selectedTile = gameBoard[row][col];
            if (selectedTile.getCurrentState() == TileStatus.OPEN) {
                moveCount++;

                selectedTile.setCurrentState(currentPlayer);
                //setting this to true tells the Adapter to set this tile's color to the primary
                //color, in order to clearly indicate to the user the last move that was made
                selectedTile.setCurrentColor(TileColor.PREVIOUS_MOVE);

                // if this was the first move of the game, there is no previous move
                if (rowOfLastMove != -1) {
                    // change the previous tile's color back to the default color
                    TicTacToeTile previouslyMovedTile = gameBoard[rowOfLastMove][columnOfLastMove];
                    previouslyMovedTile.setCurrentColor(TileColor.NORMAL);
                }
                // keep track of the index so we can update the color again after the next move
                rowOfLastMove = row;
                columnOfLastMove = col;
                gameView.displayPlayerMove();
                checkWinConditions(gridIndex);
            }
        }
    }

    private void checkWinConditions(final int gridIndex) {
        List<WinCondition> winConditions = winConditionMap.get(gridIndex);
        for (int i = 0; !gameOver && i < winConditions.size(); i++) {
            final WinCondition winCondition = winConditions.get(i);
            if (winCondition.winConditionMet()) {
                List<TicTacToeTile> winningTiles = winCondition.getTiles();
                for (TicTacToeTile tile : winningTiles) {
                    tile.setCurrentColor(TileColor.WINNER);
                }
                gameOver = true;
                gameView.displayGameWon(currentPlayer);
                break;
            }
        }

        if (!gameOver) {
            moveToNextPlayer();
            checkIfBoardIsFilled();
        }
    }

    private void moveToNextPlayer() {
        if (currentPlayer == TileStatus.PLAYER_X) {
            currentPlayer = TileStatus.PLAYER_O;
        } else {
            currentPlayer = TileStatus.PLAYER_X;
        }
        gameView.displayPlayerTurn(currentPlayer);
    }

    private void checkIfBoardIsFilled() {
        if (moveCount == gameBoardSize) {
            gameOver = true;
            gameView.displayGameDraw();
        }
    }

    @Override
    public void incrementBoardSize() {
        if (gameBoardRowSize < GAME_BOARD_MAX_ROW_SIZE) {
            gameBoardRowSize++;
            gameBoardSize = gameBoardRowSize * gameBoardRowSize;
            launchNewTicTacToeGame(true);
        }
    }

    @Override
    public void decrementBoardSize() {
        if (gameBoardRowSize > GAME_BOARD_MIN_ROW_SIZE) {
            gameBoardRowSize--;
            gameBoardSize = gameBoardRowSize * gameBoardRowSize;
            launchNewTicTacToeGame(true);
        }
    }
}
