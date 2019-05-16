package com.boxnotfound.tictactoe.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boxnotfound.tictactoe.R;
import com.boxnotfound.tictactoe.model.TileColor;
import com.boxnotfound.tictactoe.model.TileStatus;

/**
 * The main TicTacToe Activity, which implements {@link GameFragment.OnGameUpdateListener} in order
 * to receive and handle communications from the {@link GameFragment} implementation.
 */
public class GameActivity extends AppCompatActivity implements GameFragment.OnGameUpdateListener {

    /**
     * Reference to the {@link GamePresenter} component, since the buttons that are used to launch
     * a new game exist in the Activity.
     */
    private GamePresenter gamePresenter;
    /**
     * Reference to the ImageView object representing the current player's turn.
     */
    @BindView(R.id.iv_player_status) ImageView currentPlayer;
    /**
     * Reference to the TextView object that communicates the game status to the user.
     */
    @BindView(R.id.tv_game_status) TextView gameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GameFragment gameFragment = new GameFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_game_content, gameFragment);
        fragmentTransaction.commit();

        gamePresenter = new GamePresenter(gameFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.button_new_game:
                gamePresenter.launchNewTicTacToeGame(true);
                return true;
            case R.id.button_increment_board_size:
                gamePresenter.incrementBoardSize();
                return true;
            case R.id.button_decrement_board_size:
                gamePresenter.decrementBoardSize();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Update the game status to show the next player's turn.
     * @param player - The current player state.
     */
    @Override
    public void onPlayerTurnChanged(@NonNull TileStatus player) {
        gameStatus.setText(getString(R.string.game_status_label));
        updateCurrentPlayerImage(player, R.color.colorTilePreviousMove);
    }

    /**
     * Update the game status to show the winning player.
     * @param player - The winning player state.
     */
    @Override
    public void onPlayerWin(@NonNull TileStatus player) {
        gameStatus.setText(getString(R.string.game_won_label));
        updateCurrentPlayerImage(player, R.color.colorTileWinner);
    }

    /**
     * Update the game status to show the game has ended in a draw.
     */
    @Override
    public void onDraw() {
        gameStatus.setText(getString(R.string.game_draw_label));
        currentPlayer.setImageResource(android.R.color.transparent);
    }

    /**
     * Update the {@link #currentPlayer} UI.
     * @param player The current player state.
     * @param colorResource The color resource to be used.
     */
    private void updateCurrentPlayerImage(@NonNull final TileStatus player, final int colorResource) {

        int currentPlayerDrawableResource;
        if (player == TileStatus.PLAYER_O) {
            currentPlayerDrawableResource = R.drawable.ic_player_o;
        } else {
            currentPlayerDrawableResource = R.drawable.ic_player_x;
        }

        currentPlayer.setImageResource(currentPlayerDrawableResource);
        currentPlayer.setColorFilter(getResources().getColor(colorResource));
    }
}
