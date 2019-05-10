package com.boxnotfound.tictactoe.model.wincondition;


import com.boxnotfound.tictactoe.model.TicTacToeTile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;

public class WinConditionUtils {

    private static SparseArrayCompat<List<WinCondition>> winConditionMap;
    private static int gameBoardRowSize;

    private WinConditionUtils() {
        // prevent instantiation to limit usage to static methods
    }

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

    private static void setupWinConditionMap() {
        int gameBoardSize = gameBoardRowSize * gameBoardRowSize;
        for (int i = 0; i < gameBoardSize; i++) {
            winConditionMap.put(i, new ArrayList<WinCondition>());
        }
    }

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

    private static void addTileDataToLists(TicTacToeTile[][] gameBoard, ArrayList<TicTacToeTile> tiles,
                                           ArrayList<Integer> sparseArrayKeyValues, int row, int col) {

        TicTacToeTile tile = gameBoard[row][col];
        int sparseArrayKeyValue = calculateSparseArrayKeyValue(row, col);
        sparseArrayKeyValues.add(sparseArrayKeyValue);
        tiles.add(tile);
    }


    private static int calculateSparseArrayKeyValue(final int row, final int col) {
        return gameBoardRowSize * row + col;
    }

    private static void addWinConditionToMap(@NonNull final WinCondition winCondition, @NonNull final List<Integer> sparseArrayKeyValues) {
        /*
            To easily look up what winConditions are associated with each
            game board index, add the win condition to each map
        */
        for (int keyValue : sparseArrayKeyValues) {
            winConditionMap.get(keyValue).add(winCondition);
        }
    }

    public static void cleanupMap() {
        if (winConditionMap != null) {
            winConditionMap.clear();
            winConditionMap = null;
        }
    }


}
