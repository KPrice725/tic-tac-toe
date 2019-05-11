package com.boxnotfound.tictactoe.game;

import com.boxnotfound.tictactoe.BasePresenter;
import com.boxnotfound.tictactoe.BaseView;
import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileStatus;

import java.util.List;

import androidx.annotation.NonNull;

public interface GameContract {

    interface Presenter extends BasePresenter {

        void launchNewTicTacToeGame(final boolean userRequested);

        void setPlayerMove(final int gridIndex);

        void incrementBoardSize();

        void decrementBoardSize();

    }

    interface View extends BaseView<Presenter> {

        void displayNewTicTacToeGame(@NonNull final List<TicTacToeTile> tiles, final int rowSize);

        void displayPlayerMove();

        void displayPlayerTurn(@NonNull final TileStatus player);

        void displayGameWon(@NonNull final TileStatus winningPlayer);

        void displayGameDraw();
    }
}
