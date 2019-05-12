# tic-tac-toe
A simple tic tac toe game, with a twist!

![DEMO](/readme-resources/tictactoe.gif)

## Project Overview

- Displays a square gameboard grid of TicTacToe tiles, sized from 4x4 to 8x8.
- Two players, represented as Player X and Player O, take turns selecting tiles to control until one of them fulfill one of the several win conditions:
  - Row - A player occupies all tiles in a single row of the board.
  - Column - A player occupies all tiles in a single column of the board.
  - Diagonal - A player occupies all tiles in a diagonal line from opposite corners of the board.
  - Square - A player occupies four tiles that form a 2x2 square on the board.
  - Corners - A player occupies the tile on each corner of the board.
- A draw is the result if all tiles are occupied without fulfilling one of the win conditions.

### Architecture & Design

![TicTacToeArchitecture](/readme-resources/TicTacToeArchitecture.jpg)

- This application uses the Model View Presenter architecture.  The Model component is primarily driven by the TicTacToeBoard and the WinConditionUtils classes.
  - TicTacToeBoard - Generates and stores a two-dimensional array of TicTacToeTiles, representing the game board.  Each TicTacToeTile holds two member variables, TileStatus currentState & TileColor currentColor.  The TileStatus represents which player currently controls that particular tile, or if it is currently open to be selected.  The TileColor represents the current color state that the tile should be represented as.  The NORMAL state represents the default tile color, the PREVIOUS_MOVE state represents the tile color to indicate to the user the previous move.  The WINNER state represents the tile color to indicate the winning tiles, should one of the win conditions be fulfilled.
  - WinConditionUtils - Generates and stores a SparseArray map of Lists of WinCondition objects using the two-dimensional TicTacToeTile array generated from the TicTacToeBoard, representing all the possible tile combinations that would result in a win condition given the specifications above.  Each WinCondition is mapped to each tile index that it is associated with.  The Presenter uses this map to efficiently evaluate the win conditions affected by each player move.  For example, if Player X claims tile index 0 (the top left corner of the board), the Presenter updates the Tile's TileStatus, then accesses the SparseArray's WinCondition list at Key 0.  All the WinConditions in this list contain Tile index 0 as part of their respective win condition requirements.  The presenter iterates through the list, calling each WinCondition's winConditionMet() function to evaluate if the player's move resulted in a win.
  
![WinConditionMapExample](/readme-resources/WinConditionMapExample.jpg)

- The Presenter, represented by the GamePresenter class, is responsible for making calls to the TicTacToeBoard and WinConditionUtils classes to generate the needed game information, provide this information to the view to display for the UI, and to properly handle model updates resulting from user interactions.
- The View, represented by the GameFragment class, is responsible for displaying the model gameboard data communicated by the Presenter, as well as notifying the Presenter following any user interactions.
