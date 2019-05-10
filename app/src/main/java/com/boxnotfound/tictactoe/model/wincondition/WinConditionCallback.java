package com.boxnotfound.tictactoe.model.wincondition;

import com.boxnotfound.tictactoe.model.TileStatus;

import androidx.annotation.NonNull;

public interface WinConditionCallback {

    void winConditionMet(@NonNull final TileStatus winner);

    void openTileFound();

    void conflictingStatesFound();

}
