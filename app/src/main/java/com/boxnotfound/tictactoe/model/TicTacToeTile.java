package com.boxnotfound.tictactoe.model;

import androidx.annotation.NonNull;

/**
 * The model class representing the state of a single TicTacToe tile.
 */
public class TicTacToeTile {

    /**
     * The state representing who controls the tile
     * {@link TileStatus#OPEN} - This tile is open and free to be claimed by a player.
     * {@link TileStatus#PLAYER_O} - This tile is controlled by Player O.
     * {@link TileStatus#PLAYER_X} - This tile is controlled by Player X.
     */
    private TileStatus currentState;
    /**
     * The state indicating how the UI should be representing this tile.
     * {@link TileColor#NORMAL} - The standard color for tiles.
     * {@link TileColor#PREVIOUS_MOVE} - A highlighted color to indicate that this tile is the
     * tile selected during the previous move.
     * {@link TileColor#WINNER} - A highlighted color to indicate that this tile is one of the tiles
     * associated with a fulfilled {@link com.boxnotfound.tictactoe.model.wincondition.WinCondition}
     * state.
     */
    private TileColor currentColor;

    /**
     * Tiles are created upon a new game launch.  All tiles are open and normal by default.
     */
    public TicTacToeTile() {
        currentState = TileStatus.OPEN;
        currentColor = TileColor.NORMAL;
    }

    /**
     * Update the {@link #currentState} of the tile object.
     * @param currentState The player who has selected to control this tile.
     */
    public void setCurrentState(@NonNull final TileStatus currentState) {
        this.currentState = currentState;
    }

    /**
     * Retrieve the {@link #currentState}.
     * @return The current control state of the tile object.
     */
    public TileStatus getCurrentState() {
        return currentState;
    }

    /**
     * Update the {@link #currentColor} of the tile object.
     * @param currentColor The color state of the tile object.
     */
    public void setCurrentColor(@NonNull final TileColor currentColor) {
        this.currentColor = currentColor;
    }

    /**
     * Retrieve the {@link #currentColor} of the tile object.
     * @return The current color state of the tile object.
     */
    public TileColor getCurrentColor() {
        return currentColor;
    }
}
