package com.boxnotfound.tictactoe.model;

import androidx.annotation.NonNull;

public class TicTacToeTile {

    private TileStatus currentState;
    private int rowIndex;
    private int colIndex;

    public TicTacToeTile(final int rowIndex, final int colIndex) {
        currentState = TileStatus.OPEN;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public void setCurrentState(@NonNull final TileStatus currentState) {
        this.currentState = currentState;
    }

    public TileStatus getCurrentState() {
        return currentState;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
}
