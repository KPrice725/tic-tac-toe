package com.boxnotfound.tictactoe.model;

import androidx.annotation.IntRange;

/**
 * The model class that generates the {@link TicTacToeTile} objects representing the current
 * TicTacToe game board state, caching and providing them in a two-dimensional array.
 */
public class TicTacToeBoard {

    /**
     * The cache of {@link TicTacToeTile} objects.
     */
    private static TicTacToeTile[][] gameBoard;

    private TicTacToeBoard() {
        //prevent instantiation to prevent multiple game board instantiations
    }

    /**
     * Generates and returns the gameBoard array.
     * @param rowSize The number of tiles per row requested.
     * @param newGame Whether or not this is called as a result of a new game being started.
     * @return The collection of tiles representing the game board state.
     */
    public static TicTacToeTile[][] setupTicTacToeBoard(@IntRange(from = 4) final int rowSize, final boolean newGame) {
        if (gameBoard == null || newGame) {
            gameBoard = new TicTacToeTile[rowSize][rowSize];
            initializeTiles(rowSize);
        }
        return gameBoard;
    }

    /**
     * Generate each tile and insert one into each index of the {@link #gameBoard}.  The game board
     * is always in a square shape, with the equally sized rows and columns.
     * @param rowSize The number of tiles per row requested.
     */
    private static void initializeTiles(final int rowSize) {
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < rowSize; col++) {
                gameBoard[row][col] = new TicTacToeTile();
            }
        }
    }
}
