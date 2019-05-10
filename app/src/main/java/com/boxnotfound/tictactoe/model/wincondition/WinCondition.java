package com.boxnotfound.tictactoe.model.wincondition;

import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileStatus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

public class WinCondition {

    private List<TicTacToeTile> tiles;
    private WinConditionType type;

    public WinCondition(@NonNull @Size(min = 1) final List<TicTacToeTile> tiles, @NonNull final WinConditionType type) {
        this.tiles = tiles;
        this.type = type;
    }

    public List<TicTacToeTile> getTiles() {
        return tiles;
    }

    public void winConditionMet(@NonNull final WinConditionCallback callback) {

        TileStatus targetState = tiles.get(0).getCurrentState();

        if (targetState == TileStatus.OPEN) {
            // If there's an open tile, the win condition cannot have been met yet
            callback.openTileFound();
            return;
        }

        for (int i = 1; i < tiles.size(); i++) {
            TicTacToeTile tile = tiles.get(i);
            if (tile.getCurrentState() == TileStatus.OPEN) {
                // If there's an open tile, the win condition cannot have been met yet
                callback.openTileFound();
                return;
            } else if (targetState != tile.getCurrentState()) {
                /*
                Both players occupy tiles in the list, this win condition can
                never be met
                */
                callback.conflictingStatesFound();
                return;
            }
        }
        /*
        We have made it to the end of the for loop without any conflicting or open tiles,
        this win condition has been met by the target player
        */
        callback.winConditionMet(targetState);
    }

    public WinConditionType getType() {
        return type;
    }
}
