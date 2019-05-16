package com.boxnotfound.tictactoe.model.wincondition;

import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileStatus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

/**
 * The model class whose sole responsibility is to monitor a set of {@link TicTacToeTile}s
 * representing a win condition that would end the current game, given a list of tiles that
 * represent a square game board, indexed across each row, with the the first tile representing the
 * top-left corner of the board and the last tile representing the bottom-right corner of the board.
 * The win condition is ultimately met when the {@link TileStatus} of all tiles in the list are
 * equal to the same value, either {@link TileStatus#PLAYER_O} or {@link TileStatus#PLAYER_X}.
 */
public class WinCondition {

    /**
     * The list of {@link TicTacToeTile}s, whose state will be monitored to evaluate the game's
     * win condition.
     */
    private List<TicTacToeTile> tiles;
    /**
     * Indicates the type of win condition the particular object represents, given the current
     * game's rules implementation.
     */
    private WinConditionType type;

    /**
     * Every win condition needs a non-empty list of {@link TicTacToeTile}s to monitor, and needs
     * to have a {@link WinConditionType} defined.
     * @param tiles - The tile objects to be monitored.
     * @param type - The
     */
    public WinCondition(@NonNull @Size(min = 1) final List<TicTacToeTile> tiles, @NonNull final WinConditionType type) {
        this.tiles = tiles;
        this.type = type;
    }

    /**
     * Returns the list of {@link TicTacToeTile} objects.
     * @return The list of stored tile objects.
     */
    public List<TicTacToeTile> getTiles() {
        return tiles;
    }

    /**
     * Iterate through the stored list of the tiles, checking each tile's {@link TileStatus}.  If
     * any tile's status is set to {@link TileStatus#OPEN}, or if both players occupy a tile
     * in the list, the call will return false.  The call will only return true if all tiles
     * are equal to the same state, either {@link TileStatus#PLAYER_X} or
     * {@link TileStatus#PLAYER_O}.
     * @return Whether or not the win condition has been fulfilled.
     */
    public boolean winConditionMet() {

        TileStatus targetState = tiles.get(0).getCurrentState();

        if (targetState == TileStatus.OPEN) {
            // If there's an open tile, the win condition cannot have been met yet
            return false;
        }

        for (int i = 1; i < tiles.size(); i++) {
            TicTacToeTile tile = tiles.get(i);
            if (tile.getCurrentState() == TileStatus.OPEN) {
                // If there's an open tile, the win condition cannot have been met yet
                return false;
            } else if (targetState != tile.getCurrentState()) {
                /*
                Both players occupy tiles in the list, this win condition can
                never be met
                */
                return false;
            }
        }
        /*
        We have made it to the end of the for loop without any conflicting or open tiles,
        this win condition has been met by the target player
        */
        return true;
    }

    /**
     * Retrieve the {@link WinConditionType}.
     * @return The set {@link WinConditionType}.
     */
    public WinConditionType getType() {
        return type;
    }
}
