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

    @Before
    public void setup() {
        gameBoard = TicTacToeBoard.setupTicTacToeBoard(ROW_SIZE, true);
    }

    @Test
    public void checkGameBoard() {
        assertThat(gameBoard, notNullValue());
    }

    @Test
    public void checkTiles() {
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < ROW_SIZE; col++) {
                TicTacToeTile tile = gameBoard[row][col];
                assertThat(tile, notNullValue());
            }
        }
    }

    @Test
    public void checkProperNumberOfTilesInBoard() {
        int numberOfTiles = ROW_SIZE * ROW_SIZE;
        int tileCounter = 0;
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < ROW_SIZE; col++) {
                tileCounter++;
            }
        }
        assert(numberOfTiles == tileCounter);
    }


    @Test
    public void checkGameBoardRowCount() {
        int numRows = gameBoard.length;
        assertThat(numRows, is(ROW_SIZE));
    }

    @Test
    public void checkGameBoardColumnCount() {
        for (TicTacToeTile[] row : gameBoard) {
            assertThat(row.length, is(ROW_SIZE));
        }
    }

    @After
    public void cleanup() {
        gameBoard = null;
    }
}
