package com.boxnotfound.tictactoe;

import com.boxnotfound.tictactoe.model.TicTacToeBoard;
import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileColor;
import com.boxnotfound.tictactoe.model.TileStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TicTacToeTileTest {


    private static final int ROW_SIZE = 4;
    private TicTacToeTile[][] gameBoard;

    @Before
    public void setup() {
        gameBoard = TicTacToeBoard.setupTicTacToeBoard(ROW_SIZE, true);
    }

    @Test
    public void ensureDefaultTileState() {
        TicTacToeTile tile = gameBoard[0][0];
        assertThat(tile.getCurrentColor(), is(TileColor.NORMAL));
        assertThat(tile.getCurrentState(), is(TileStatus.OPEN));
    }

    @Test
    public void updateTileState_retrieveAndCheckUpdatedTile() {
        TicTacToeTile tileToUpdate = gameBoard[0][0];
        gameBoard[0][0].setCurrentState(TileStatus.PLAYER_O);
        assertThat(tileToUpdate.getCurrentState(), is(TileStatus.PLAYER_O));
    }

    @Test
    public void updateTileColor_retrieveAndCheckUpdatedTile() {
        TicTacToeTile tileToUpdate = gameBoard[0][0];
        gameBoard[0][0].setCurrentColor(TileColor.PREVIOUS_MOVE);
        assertThat(tileToUpdate.getCurrentColor(), is(TileColor.PREVIOUS_MOVE));
    }

    @After
    public void cleanup() {
        gameBoard = null;
    }
}
