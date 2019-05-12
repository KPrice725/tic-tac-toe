package com.boxnotfound.tictactoe.model;

import androidx.annotation.IntRange;

public class TicTacToeBoard {

    private static TicTacToeTile[][] gameBoard;

    private TicTacToeBoard() {
        //prevent instantiation to prevent multiple game board instantiations
    }

    public static TicTacToeTile[][] setupTicTacToeBoard(@IntRange(from = 4) final int rowSize, final boolean newGame) {
        if (gameBoard == null || newGame) {
            gameBoard = new TicTacToeTile[rowSize][rowSize];
            initializeTiles(rowSize);
        }
        return gameBoard;
    }

    /*
    Set up the tic tac toe game board to be a square of Tiles
    */
    private static void initializeTiles(final int rowSize) {
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < rowSize; col++) {
                gameBoard[row][col] = new TicTacToeTile();
            }
        }
    }
}
