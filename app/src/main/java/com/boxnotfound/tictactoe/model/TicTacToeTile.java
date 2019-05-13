package com.boxnotfound.tictactoe.model;

import androidx.annotation.NonNull;

public class TicTacToeTile {

    private TileStatus currentState;
    private TileColor currentColor;

    public TicTacToeTile() {
        currentState = TileStatus.OPEN;
        currentColor = TileColor.NORMAL;
    }

    public void setCurrentState(@NonNull final TileStatus currentState) {
        this.currentState = currentState;
    }

    public TileStatus getCurrentState() {
        return currentState;
    }

    public void setCurrentColor(@NonNull final TileColor currentColor) {
        this.currentColor = currentColor;
    }

    public TileColor getCurrentColor() {
        return currentColor;
    }
}
