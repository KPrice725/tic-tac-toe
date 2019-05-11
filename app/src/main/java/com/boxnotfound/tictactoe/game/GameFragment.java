package com.boxnotfound.tictactoe.game;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.boxnotfound.tictactoe.R;
import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileColor;
import com.boxnotfound.tictactoe.model.TileStatus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment implements GameContract.View {

    private GameContract.Presenter presenter;
    private GameBoardAdapter adapter;
    private OnGameUpdateListener gameUpdateListener;
    private GridView gameBoardView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGameUpdateListener) {
            gameUpdateListener = (OnGameUpdateListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnPlayerTurnChangedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameBoardView = view.findViewById(R.id.layout_game_board);
        adapter = new GameBoardAdapter();
        gameBoardView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gameUpdateListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.launchNewTicTacToeGame(false);
    }

    @Override
    public void displayNewTicTacToeGame(@NonNull final List<TicTacToeTile> tiles, final int rowSize) {
        Log.d("GameFragment", "rowSize: " + rowSize + "; listSize: " + tiles.size());
        gameBoardView.setNumColumns(rowSize);
        adapter.setTiles(tiles);
    }

    @Override
    public void displayPlayerMove() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void displayPlayerTurn(@NonNull final TileStatus player) {
        // communicate this to the activity, since the current player turn UI exists there
        gameUpdateListener.onPlayerTurnChanged(player);
    }

    @Override
    public void displayGameWon(@NonNull final TileStatus winningPlayer) {
        adapter.notifyDataSetChanged();
        gameUpdateListener.onPlayerWin(winningPlayer);
    }

    @Override
    public void displayGameDraw() {
        gameUpdateListener.onDraw();
    }

    @Override
    public void setPresenter(@NonNull GameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private class GameBoardAdapter extends BaseAdapter {

        private List<TicTacToeTile> tiles;

        public void setTiles(@NonNull final List<TicTacToeTile> tiles) {
            this.tiles = tiles;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (tiles == null) {
                return 0;
            } else {
                return tiles.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TicTacToeTile tile = tiles.get(position);
            TileStatus currentState = tile.getCurrentState();
            TileColor currentColor = tile.getCurrentColor();

            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(R.layout.game_tile_item, parent, false);
            } else {
                Log.d("GameFragment", "convertView not null: " + position);
                int convertViewWidth = convertView.getMeasuredWidth();
                Log.d("GameFragment", "CV width: " + convertViewWidth);
            }
            TileImageView tileImage = convertView.findViewById(R.id.iv_game_tile);
            if (convertView != null) {
                int viewHeight = parent.getLayoutParams().height;
                Log.d("GameFragment", "viewHeight: " + viewHeight);
            }

            if (currentState == TileStatus.PLAYER_O) {
                tileImage.setImageResource(R.drawable.ic_player_o);
            } else if (currentState == TileStatus.PLAYER_X) {
                tileImage.setImageResource(R.drawable.ic_player_x);
            } else {
                tileImage.setImageResource(android.R.color.transparent);
            }

            if (currentColor == TileColor.PREVIOUS_MOVE) {
                tileImage.setColorFilter(getResources().getColor(R.color.colorTilePreviousMove));
            } else if (currentColor == TileColor.WINNER) {
                tileImage.setColorFilter(getResources().getColor(R.color.colorTileWinner));
            } else {
                tileImage.setColorFilter(getResources().getColor(R.color.colorTileNormal));
            }

            tileImage.setOnClickListener(v -> presenter.setPlayerMove(position));

            return tileImage;
        }
    }

    public static class TileImageView extends AppCompatImageView {

        public TileImageView(Context context) {
            super(context);
        }

        public TileImageView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TileImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

    public interface OnGameUpdateListener {
        void onPlayerTurnChanged(@NonNull final TileStatus player);

        void onPlayerWin(@NonNull final TileStatus player);

        void onDraw();
    }
}
