package com.boxnotfound.tictactoe.game;

import com.boxnotfound.tictactoe.model.TicTacToeBoard;
import com.boxnotfound.tictactoe.model.TicTacToeTile;
import com.boxnotfound.tictactoe.model.TileColor;
import com.boxnotfound.tictactoe.model.TileStatus;
import com.boxnotfound.tictactoe.model.wincondition.WinCondition;
import com.boxnotfound.tictactoe.model.wincondition.WinConditionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;

/**
 * The Presenter Component of the TicTacToe game board
 */
public class GamePresenter implements GameContract.Presenter {

    /**
     * Hold a reference to the {@link GameContract.View} Component so the Presenter can communicate
     * UI updates as necessary.
     */
    private GameContract.View gameView;
    /**
     * The primary collection of {@link TicTacToeTile}s representing the state of the game board
     */
    private TicTacToeTile[][] gameBoard;
    /**
     * Lower limit for the size of each game board row
     */
    private static final int GAME_BOARD_MIN_ROW_SIZE = 4;
    /** Upper limit for the size of each game board row.  This could be higher, but it
     *  may negatively affect UX by forcing the UI to shrink to a point that becomes hard for the
     *  user to click tiles.
     */
    private static final int GAME_BOARD_MAX_ROW_SIZE = 8;
    /**
     * The default starting game board row size
     */
    private static int gameBoardRowSize = 4;
    /**
     * The total number of tiles in the square game board.
     */
    private static int gameBoardSize = gameBoardRowSize * gameBoardRowSize;
    /**
     * The player set to make the next game move.
     */
    private static TileStatus currentPlayer;
    /** When a game is won, cache the winning player so it is retained in instances of Activity
     *  rebuild prior to a new game being started.
     */
    private static TileStatus winningPlayer;
    /** Flag to indicate if the current game has been completed.  If yes, no more user moves are
     *  allowed until a call to start a new game has been issued by the user.
     */
    private static boolean gameOver;
    /** Immediately set to false after initial application launch, this allows the game to start
     *  automatically after being called by the View Component once all application components
     *  have been set up.  After this instance, only the user should issue requests to start
     *  a new game.
     */
    private static boolean firstLaunch = true;
    /** Tracks the row and column index of the tile that was last selected during the previous
     *  player's move.  When a tile is initially selected, the Presenter calls its
     *  {@link TicTacToeTile#setCurrentColor(TileColor)} method, passing in
     *  {@link TileColor#PREVIOUS_MOVE}.  This is a state that the View component then uses to
     *  specifically highlight the tile that was last selected.  When the next player makes their
     *  move, the rowOfLastMove and columnOfLastMove are used to update that previous tile's
     *  {@link TicTacToeTile#currentColor} again, this time passing in {@link TileColor#NORMAL},
     *  which is used by the View component to display a normal tile image.
     */
    private static int rowOfLastMove, columnOfLastMove;
    /** Tracks the number of moves made in the current game.  If this value equals
     *  {@link #gameBoardSize}, it indicates that there are no more moves that can be made.
     */
    private static int moveCount;
    /** The cached map of Lists of {@link WinCondition} objects provided by
     *  {@link WinConditionUtils#generateWinConditionsFromTicTacToeBoard(TicTacToeTile[][])}
     *  associated with the current game's
     *  {@link #gameBoard}, mapped by the grid index value, with 0 representing the top left
     *  corner of the game board, and ({@link #gameBoardSize} - 1) representing the bottom right
     *  corner of the game board.  When a player makes a move, the View passes the game board
     *  index to the Presenter, which can be used to access the list of {@link WinCondition}
     *  objects containing the tile associated with that particular index.  This allows the
     *  Presenter to focus solely on checking the conditions that are only impacted by the
     *  previous player move.
     */
    private static SparseArrayCompat<List<WinCondition>> winConditionMap;

    /**
     * Constructor requires a {@link GameContract.View} component in order to be able to communicate
     *      *  updates for the View.
     * @param gameView
     */
    public GamePresenter(@NonNull final GameContract.View gameView) {
        this.gameView = gameView;
        gameView.setPresenter(this);
    }

    /**
     * Called by the View component when the LifeCycle state has reached onResume.  This is the
     * first method called by the View, and is not initiated by the user.
     */
    @Override
    public void start() {
        launchNewTicTacToeGame(false);
    }

    /**
     * Request the {@link TicTacToeBoard} generate and cache a new {@link #gameBoard}, set up
     * the initial game parameters, and communicate the game state to the View if this is the
     * initial application launch or if the user has requested a new game be started.  If neither
     * of these are the case, such as during screen orientation change or the OS calling onResume
     * after onPause, simply retrieve the cached game board and pass that state the the View.
     * @param userRequested If the user has pushed one of the buttons that request a new game.
     */
    @Override
    public void launchNewTicTacToeGame(final boolean userRequested) {
        if (userRequested || firstLaunch) {
            gameBoard = TicTacToeBoard.setupTicTacToeBoard(gameBoardRowSize, true);
            firstLaunch = false;
            gameOver = false;
            winningPlayer = TileStatus.OPEN;
            rowOfLastMove = columnOfLastMove = -1;
            moveCount = 0;
            currentPlayer = TileStatus.PLAYER_X;
            setupWinConditions();
        } else {
            gameBoard = TicTacToeBoard.setupTicTacToeBoard(gameBoardRowSize, false);
        }

        if (!gameOver) {
            gameView.displayPlayerTurn(currentPlayer);
        } else {
            if (winningPlayer != TileStatus.OPEN) {
                gameView.displayGameWon(winningPlayer);
            } else {
                gameView.displayGameDraw();
            }
        }
        setupTileListForView();
    }

    /**
     *  Call {@link WinConditionUtils#generateWinConditionsFromTicTacToeBoard(TicTacToeTile[][])}
     *  to generate the map of lists of {@link WinCondition} objects that monitor the game status
     *  and notify if/when the game has been won.
     */
    private void setupWinConditions() {

        if (winConditionMap != null) {
            winConditionMap.clear();
        }

        winConditionMap = WinConditionUtils.generateWinConditionsFromTicTacToeBoard(gameBoard);
    }

    /**
     * Convert the two-dimensional {@link TicTacToeTile} array into an ArrayList, which is passed
     * to the view and can be used by its
     * {@link com.boxnotfound.tictactoe.game.GameFragment.GameBoardAdapter} to display the state of
     * the current game.
     */
    private void setupTileListForView() {
        List<TicTacToeTile> tiles = new ArrayList<>();
        for (TicTacToeTile[] tileRow : gameBoard) {
            tiles.addAll(Arrays.asList(tileRow));
        }
        gameView.displayNewTicTacToeGame(tiles, gameBoardRowSize);
    }

    /**
     * Called by the View when a player has clicked one of the game board's tiles.  If the game is
     * not over, and the tile's {@link TileStatus} is set to {@link TileStatus#OPEN}, set that
     * tile's status to match the player that selected it.  Additionally, this sets the tile's
     * {@link TileColor} to {@link TileColor#PREVIOUS_MOVE}, which indicates to the View that it
     * should highlight this tile.
     * @param gridIndex The index of the tile selected.
     */
    @Override
    public void setPlayerMove(final int gridIndex) {
        if (!gameOver) {
            int row = gridIndex / gameBoardRowSize;
            int col = gridIndex % gameBoardRowSize;
            TicTacToeTile selectedTile = gameBoard[row][col];
            if (selectedTile.getCurrentState() == TileStatus.OPEN) {
                moveCount++;

                selectedTile.setCurrentState(currentPlayer);
                //setting this to true tells the Adapter to set this tile's color to the primary
                //color, in order to clearly indicate to the user the last move that was made
                selectedTile.setCurrentColor(TileColor.PREVIOUS_MOVE);

                // if this was the first move of the game, there is no previous move
                if (rowOfLastMove != -1) {
                    // change the previous tile's color back to the default color
                    TicTacToeTile previouslyMovedTile = gameBoard[rowOfLastMove][columnOfLastMove];
                    previouslyMovedTile.setCurrentColor(TileColor.NORMAL);
                }
                // keep track of the index so we can update the color again after the next move
                rowOfLastMove = row;
                columnOfLastMove = col;
                gameView.displayPlayerMove();
                checkWinConditions(gridIndex);
            }
        }
    }

    /**
     * Retrieve the list of {@link TicTacToeTile} from the {@link #winConditionMap} using the index
     * provided by the View, iterating through each one, and calling each {@link WinCondition}
     * object's respective {@link WinCondition#winConditionMet()} method.  If this returns true,
     * the game has been won by the current player.  If no conditions are met, we can proceed to
     * the next player's move and then check if there are any moves left.
     * @param gridIndex The index of the tile selected.
     */
    private void checkWinConditions(final int gridIndex) {
        List<WinCondition> winConditions = winConditionMap.get(gridIndex);
        for (int i = 0; !gameOver && i < winConditions.size(); i++) {
            final WinCondition winCondition = winConditions.get(i);
            if (winCondition.winConditionMet()) {
                List<TicTacToeTile> winningTiles = winCondition.getTiles();
                for (TicTacToeTile tile : winningTiles) {
                    tile.setCurrentColor(TileColor.WINNER);
                }
                gameOver = true;
                winningPlayer = currentPlayer;
                gameView.displayGameWon(winningPlayer);
                break;
            }
        }

        if (!gameOver) {
            moveToNextPlayer();
            checkIfBoardIsFilled();
        }
    }

    /**
     * After the current player has made their move, assign the other player as the current player
     * and update the View.
     */
    private void moveToNextPlayer() {
        if (currentPlayer == TileStatus.PLAYER_X) {
            currentPlayer = TileStatus.PLAYER_O;
        } else {
            currentPlayer = TileStatus.PLAYER_X;
        }
        gameView.displayPlayerTurn(currentPlayer);
    }

    /**
     * Check to see if there are any more moves that can be made.  If not, the game is over as a
     * Draw.
     */
    private void checkIfBoardIsFilled() {
        if (moveCount == gameBoardSize) {
            gameOver = true;
            gameView.displayGameDraw();
        }
    }

    /**
     * If the {@link #gameBoardRowSize} is less than {@link #GAME_BOARD_MAX_ROW_SIZE}, increase
     * the row size by one and launch a new game.  This will add a column and a row to the
     * game board.
     */
    @Override
    public void incrementBoardSize() {
        if (gameBoardRowSize < GAME_BOARD_MAX_ROW_SIZE) {
            gameBoardRowSize++;
            gameBoardSize = gameBoardRowSize * gameBoardRowSize;
            launchNewTicTacToeGame(true);
        }
    }

    /**
     * If the {@link #gameBoardRowSize} is greater than {@link #GAME_BOARD_MIN_ROW_SIZE}, decrease
     * the row size by one and launch a new game.  This will remove a column and a row from the
     * game board.
     */
    @Override
    public void decrementBoardSize() {
        if (gameBoardRowSize > GAME_BOARD_MIN_ROW_SIZE) {
            gameBoardRowSize--;
            gameBoardSize = gameBoardRowSize * gameBoardRowSize;
            launchNewTicTacToeGame(true);
        }
    }
}
