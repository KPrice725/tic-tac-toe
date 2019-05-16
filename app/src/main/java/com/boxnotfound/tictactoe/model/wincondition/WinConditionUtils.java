package com.boxnotfound.tictactoe.model.wincondition;


import com.boxnotfound.tictactoe.model.TicTacToeTile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;

/**
 * Generates all {@link WinCondition} objects and maps them according to their associated
 * tile index values.  For this version of TicTacToe, the win condition types are:
 *
 * {@link WinConditionType#ROW}: When a player controls all tiles in a single row
 * {@link WinConditionType#COLUMN}: When a player controls all tiles in a single column
 * {@link WinConditionType#DIAGONAL}: When a player controls all tiles in one of the two diagonal lines
 * {@link WinConditionType#SQUARE}: When a player controls four tiles forming a 2x2 square shape
 * {@link WinConditionType#CORNERS}: When a player controls the tiles in the four corners of the board
 */
public class WinConditionUtils {

    /**
     * The map of lists of {@link WinCondition} objects arranged by game board index.
     */
    private static SparseArrayCompat<List<WinCondition>> winConditionMap;
    /**
     * The number of tiles on a single row of the game board.
     */
    private static int gameBoardRowSize;

    private WinConditionUtils() {
        // prevent instantiation to limit usage to static methods
    }

    /**
     * Instantiates the map and populates it by calling the various setup methods for the different
     * win condition types.
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     * @return The fully populated map of {@link WinCondition} lists.
     */
    public static SparseArrayCompat<List<WinCondition>> generateWinConditionsFromTicTacToeBoard(@NonNull final TicTacToeTile[][] gameBoard) {
        if (winConditionMap == null) {
            winConditionMap = new SparseArrayCompat<>();
        } else {
            winConditionMap.clear();
        }

        gameBoardRowSize = gameBoard.length;

        setupWinConditionMap();
        setupRowWinConditions(gameBoard);
        setupColumnWinConditions(gameBoard);
        setupDiagonalWinConditions(gameBoard);
        setupSquareWinConditions(gameBoard);
        setupCornersWinConditions(gameBoard);

        return winConditionMap;
    }

    /**
     * Add a blank ArrayList to each index of the map, based on the size of the game board.
     */
    private static void setupWinConditionMap() {
        int gameBoardSize = gameBoardRowSize * gameBoardRowSize;
        for (int i = 0; i < gameBoardSize; i++) {
            winConditionMap.put(i, new ArrayList<WinCondition>());
        }
    }

    /**
     * Generates all {@link WinCondition} objects with {@link WinConditionType#ROW} properties.
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     */
    private static void setupRowWinConditions(@NonNull final TicTacToeTile[][] gameBoard) {

        WinConditionType type = WinConditionType.ROW;

        for (int row = 0; row < gameBoardRowSize; row++) {
            ArrayList<Integer> sparseArrayKeyValues = new ArrayList<>();
            ArrayList<TicTacToeTile> tiles = new ArrayList<>();
            for (int col = 0; col < gameBoardRowSize; col++) {
                addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);
            }
            WinCondition rowWinCondition = new WinCondition(tiles, type);
            addWinConditionToMap(rowWinCondition, sparseArrayKeyValues);
        }
    }

    /**
     * Generates all {@link WinCondition} objects with {@link WinConditionType#COLUMN} properties.
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     */
    private static void setupColumnWinConditions(@NonNull final TicTacToeTile[][] gameBoard) {

        WinConditionType type = WinConditionType.COLUMN;

        for (int col = 0; col < gameBoardRowSize; col++) {
            ArrayList<Integer> sparseArrayKeyValues = new ArrayList<>();
            ArrayList<TicTacToeTile> tiles = new ArrayList<>();
            for (int row = 0; row < gameBoardRowSize; row++) {
                addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);
            }

            WinCondition colWinCondition = new WinCondition(tiles, type);
            addWinConditionToMap(colWinCondition, sparseArrayKeyValues);
        }
    }

    /**
     * Generates all {@link WinCondition} objects with {@link WinConditionType#DIAGONAL} properties.
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     */
    private static void setupDiagonalWinConditions(@NonNull final TicTacToeTile[][] gameBoard) {

        WinConditionType type = WinConditionType.DIAGONAL;

        // top left to bottom right diagonal
        int row = 0;
        int col = 0;
        ArrayList<Integer> sparseArrayKeyValues = new ArrayList<>();
        ArrayList<TicTacToeTile> tiles = new ArrayList<>();
        for (; row < gameBoardRowSize && col < gameBoardRowSize; row++, col++) {
            addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);
        }

        WinCondition diagonalWinCondition = new WinCondition(tiles, type);
        addWinConditionToMap(diagonalWinCondition, sparseArrayKeyValues);

        // top right to bottom left diagonal
        row = 0;
        col = gameBoardRowSize - 1;
        sparseArrayKeyValues = new ArrayList<>();
        tiles = new ArrayList<>();
        for (; row < gameBoardRowSize && col >= 0; row++, col--) {
            addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);
        }

        diagonalWinCondition = new WinCondition(tiles, type);
        addWinConditionToMap(diagonalWinCondition, sparseArrayKeyValues);
    }

    /**
     * Generates all {@link WinCondition} objects with {@link WinConditionType#SQUARE} properties.
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     */
    private static void setupSquareWinConditions(@NonNull final TicTacToeTile[][] gameBoard) {

        WinConditionType type = WinConditionType.SQUARE;

        for (int row = 0; row < gameBoardRowSize - 1; row++) {
            for (int col = 0; col < gameBoardRowSize - 1; col++) {
                ArrayList<Integer> sparseArrayKeyValues = new ArrayList<>();
                ArrayList<TicTacToeTile> tiles = new ArrayList<>();
                addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);
                addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row + 1, col);
                addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col + 1);
                addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row + 1, col + 1);
                WinCondition squareWinCondition = new WinCondition(tiles, type);
                addWinConditionToMap(squareWinCondition, sparseArrayKeyValues);
            }
        }
    }

    /**
     * Generates all {@link WinCondition} objects with {@link WinConditionType#CORNERS} properties.
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     */
    private static void setupCornersWinConditions(@NonNull final TicTacToeTile[][] gameBoard) {

        WinConditionType type = WinConditionType.CORNERS;

        ArrayList<Integer> sparseArrayKeyValues = new ArrayList<>();
        ArrayList<TicTacToeTile> tiles = new ArrayList<>();

        //upper left corner
        int row = 0;
        int col = 0;
        addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);

        //upper right corner
        col = gameBoardRowSize - 1;
        addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);

        //bottom right corner
        row = gameBoardRowSize - 1;
        addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);

        //bottom left corner
        col = 0;
        addTileDataToLists(gameBoard, tiles, sparseArrayKeyValues, row, col);

        WinCondition cornersWinCondition = new WinCondition(tiles, type);
        addWinConditionToMap(cornersWinCondition, sparseArrayKeyValues);
    }

    /**
     * Helper method for adding tile object data to each {@link WinCondition} object's tile list,
     * along with converting and adding the associated tile index to a key list to be used later
     * to add the resulting {@link WinCondition} object to the {@link #winConditionMap}.
     *
     * @param gameBoard The collection of {@link TicTacToeTile} objects representing the game board.
     * @param tiles The list of tiles that will be used to create the new {@link WinCondition}
     *              object.
     * @param sparseArrayKeyValues The list of index values of the tiles stored in the tiles param.
     * @param row The row index of the tile on the game board.
     * @param col The column index of the tile on the game board.
     */
    private static void addTileDataToLists(@NonNull final TicTacToeTile[][] gameBoard,
                                           @NonNull final ArrayList<TicTacToeTile> tiles,
                                           @NonNull final ArrayList<Integer> sparseArrayKeyValues,
                                           final int row, final int col) {

        TicTacToeTile tile = gameBoard[row][col];
        int sparseArrayKeyValue = calculateSparseArrayKeyValue(row, col);
        sparseArrayKeyValues.add(sparseArrayKeyValue);
        tiles.add(tile);
    }

    /**
     * Convert the row and column index values from the game board to a single index value, based on
     * the formula (rowSize * rowIndex) + columnIndex
     *
     * For example, a 4 x 4 game board represented two-dimensionally as:
     *          {0, 0} {0, 1} {0, 2} {0, 3}
     *          {1, 0} {1, 1} {1, 2} {1, 3}
     *          {2, 0} {2, 1} {2, 2} {2, 3}
     *          {3, 0} {3, 1} {3, 2} {3, 3}
     * This would be translate into:
     *          00  01  02  03
     *          04  05  06  07
     *          08  09  10  11
     *          12  13  14  15
     *
     * @param row The row index of the tile on the game board.
     * @param col The column index of the tile on the game board.
     * @return The converted index value.
     */
    private static int calculateSparseArrayKeyValue(final int row, final int col) {
        return gameBoardRowSize * row + col;
    }

    /**
     * Inserts the {@link WinCondition} object into the associated index values of the
     * {@link #winConditionMap}.
     * @param winCondition The {@link WinCondition} object to store.
     * @param sparseArrayKeyValues The map index values to insert the winCondition parameter into.
     */
    private static void addWinConditionToMap(@NonNull final WinCondition winCondition, @NonNull final List<Integer> sparseArrayKeyValues) {

        for (int keyValue : sparseArrayKeyValues) {
            winConditionMap.get(keyValue).add(winCondition);
        }
    }
}
