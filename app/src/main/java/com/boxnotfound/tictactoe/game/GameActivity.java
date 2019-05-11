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

public class GameActivity extends AppCompatActivity implements GameFragment.OnGameUpdateListener {

    private static final String GAME_LABEL_KEY = "game_label_key";
    private static final String PLAYER_IMAGE_KEY = "player_image_key";

    private GamePresenter gamePresenter;
    private int currentPlayerDrawableResource;
    @BindView(R.id.iv_player_status)
    ImageView currentPlayer;
    @BindView(R.id.tv_game_status)
    TextView gameStatus;

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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GAME_LABEL_KEY, gameStatus.getText().toString());
        outState.putInt(PLAYER_IMAGE_KEY, currentPlayerDrawableResource);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String gameStatusText = savedInstanceState.getString(GAME_LABEL_KEY);
            gameStatus.setText(gameStatusText);
            int currentPlayerColorResource;
            if (gameStatusText.equals(getString(R.string.game_won_label))) {
                currentPlayerColorResource = R.color.colorTileWinner;
            } else {
                currentPlayerColorResource = R.color.colorTilePreviousMove;
            }
            currentPlayerDrawableResource = savedInstanceState.getInt(PLAYER_IMAGE_KEY);
            currentPlayer.setImageResource(currentPlayerDrawableResource);
            currentPlayer.setColorFilter(getResources().getColor(currentPlayerColorResource));

        }
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

    @Override
    public void onPlayerTurnChanged(@NonNull TileStatus player) {
        gameStatus.setText(getString(R.string.game_status_label));
        updateCurrentPlayerImage(player, R.color.colorTilePreviousMove);
    }

    @Override
    public void onPlayerWin(@NonNull TileStatus player) {
        gameStatus.setText(getString(R.string.game_won_label));
        updateCurrentPlayerImage(player, R.color.colorTileWinner);
    }

    @Override
    public void onDraw() {
        gameStatus.setText(getString(R.string.game_draw_label));
        currentPlayerDrawableResource = android.R.color.transparent;
        currentPlayer.setImageResource(currentPlayerDrawableResource);
    }

    private void updateCurrentPlayerImage(@NonNull final TileStatus player, final int colorResource) {

        if (player == TileStatus.PLAYER_O) {
            currentPlayerDrawableResource = R.drawable.ic_player_o;
        } else {
            currentPlayerDrawableResource = R.drawable.ic_player_x;
        }

        currentPlayer.setImageResource(currentPlayerDrawableResource);
        currentPlayer.setColorFilter(getResources().getColor(colorResource));
    }

}
