package com.boxnotfound.tictactoe;

import com.boxnotfound.tictactoe.model.TicTacToeBoard;
import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileStatus;
import com.boxnotfound.tictactoe.model.wincondition.WinCondition;
import com.boxnotfound.tictactoe.model.wincondition.WinConditionType;
import com.boxnotfound.tictactoe.model.wincondition.WinConditionUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameBoardTest {

    private static final int ROW_SIZE = 4;
    private TicTacToeTile[][] gameBoard;
    private SparseArrayCompat<List<WinCondition>> winConditionMap;


    @Before
    public void setup() {
        gameBoard = TicTacToeBoard.setupTicTacToeBoard(ROW_SIZE, true);
        winConditionMap = WinConditionUtils.generateWinConditionsFromTicTacToeBoard(gameBoard);
    }

    @Test
    public void checkGameBoard() {
        assertThat(gameBoard, notNullValue());
    }

    @Test
    public void checkWinConditionMap() {
        assertThat(winConditionMap, notNullValue());
    }

    @Test
    public void checkGameBoardRowSize() {
        int numRows = gameBoard.length;
        assertThat(numRows, is(ROW_SIZE));
    }

    @Test
    public void checkGameBoardColumnSize() {
        for (TicTacToeTile[] row : gameBoard) {
            assertThat(row.length, is(ROW_SIZE));
        }
    }

    @Test
    public void ensureWinConditionsMapContainsAppropriateWinConditions() {
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < ROW_SIZE; col++) {
                TicTacToeTile tile = gameBoard[row][col];
                int gridIndex = ROW_SIZE * row + col;
                List<WinCondition> winConditions = winConditionMap.get(gridIndex);
                assertThat(winConditions, notNullValue());
                for (WinCondition winCondition : winConditions) {
                    assertThat(winCondition.getTiles().contains(tile), is(true));
                }
            }
        }
    }

    @Test
    public void checkRowWinCondition() {
        for (int col = 0; col < ROW_SIZE; col++) {
            gameBoard[0][col].setCurrentState(TileStatus.PLAYER_O);
        }
        List<WinCondition> winConditions = winConditionMap.get(0);
        for (WinCondition winCondition : winConditions) {
            if (winCondition.getType() == WinConditionType.ROW) {
                ensureWinConditionMet(winCondition);
                break;
            }
        }
    }

    @Test
    public void checkColumnWinCondition() {
        for (int row = 0; row < ROW_SIZE; row++) {
            gameBoard[row][0].setCurrentState(TileStatus.PLAYER_O);
        }
        List<WinCondition> winConditions = winConditionMap.get(0);
        for (WinCondition winCondition : winConditions) {
            if (winCondition.getType() == WinConditionType.ROW) {
                ensureWinConditionMet(winCondition);
                break;
            }
        }
    }

    @Test
    public void checkDiagonalWinCondition() {
        int row = 0;
        int col = 0;
        for (; col < ROW_SIZE && row < ROW_SIZE; row++, col++) {
            gameBoard[row][col].setCurrentState(TileStatus.PLAYER_O);
        }
        List<WinCondition> winConditions = winConditionMap.get(0);
        for (WinCondition winCondition : winConditions) {
            if (winCondition.getType() == WinConditionType.DIAGONAL) {
                ensureWinConditionMet(winCondition);
                break;
            }
        }
    }

    @Test
    public void checkSquareWinCondition() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                gameBoard[row][col].setCurrentState(TileStatus.PLAYER_O);
            }
        }
        List<WinCondition> winConditions = winConditionMap.get(0);
        for (WinCondition winCondition : winConditions) {
            if (winCondition.getType() == WinConditionType.SQUARE) {
                ensureWinConditionMet(winCondition);
                break;
            }
        }
    }

    @Test
    public void checkCornersWinCondition() {
        gameBoard[0][0].setCurrentState(TileStatus.PLAYER_O);
        gameBoard[0][ROW_SIZE - 1].setCurrentState(TileStatus.PLAYER_O);
        gameBoard[ROW_SIZE - 1][0].setCurrentState(TileStatus.PLAYER_O);
        gameBoard[ROW_SIZE - 1][ROW_SIZE - 1].setCurrentState(TileStatus.PLAYER_O);
        List<WinCondition> winConditions = winConditionMap.get(0);
        for (WinCondition winCondition : winConditions) {
            if (winCondition.getType() == WinConditionType.CORNERS) {
                ensureWinConditionMet(winCondition);
                break;
            }
        }
    }

    private void ensureWinConditionMet(@NonNull final WinCondition winCondition) {
        assertThat(winCondition.winConditionMet(), is(true));
    }

    @After
    public void cleanup() {
        TicTacToeBoard.cleanupTicTacToeBoard();
        WinConditionUtils.cleanupMap();
        gameBoard = null;
        winConditionMap = null;
    }
}
