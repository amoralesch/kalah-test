package com.edaware.examples.kalah.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Board {
  private static final Logger log = LoggerFactory.getLogger(Board.class);
  
  private static final int DEFAULT_HOUSES = 6;
  
  private static final int DEFAULT_SEEDS = 4;

  // XXX: convert to enum
  public static final int PLAYER_ONE = 0;
  public static final int PLAYER_TWO = 1;
  
  private final int houses;
  
  private final int initialSeeds;
  
  private int[] board;
  
  private int currentPlayer = PLAYER_ONE;
  
  public Board() {
    this(DEFAULT_HOUSES);
  }
  
  public Board(int houses) {
    this(houses, DEFAULT_SEEDS);
  }
  
  public Board(int houses, int seeds) {
    if (houses <= 0 || seeds <= 0)
      throw new IllegalArgumentException("invalid setup values");
    
    this.houses = houses;
    initialSeeds = seeds;
    
    initBoard();
  }
  
  public int getTotalPits() {
    return board.length;
  }
  
  public int getInitialSeeds() {
    return initialSeeds;
  }

  /**
   * Creates the board and set the correct amount of seeds in each pit
   */
  private void initBoard() {
    // each player gets _houses_ pits (times 2)
    // plus 1 store each (plus 2)
    board = new int[(houses * 2) + 2];
    
    // the setup of the board will be:
    // [0] to [houses - 1] -> player one houses
    // [houses] -> player one store
    // [houses + 1] to [houses * 2] -> player two houses
    // [houses * 2 + 1] -> player 2 store
    
    for (int i = 0; i < houses; i++)
      board[i] = initialSeeds;

    for (int i = (houses + 1); i <= (houses * 2); i++)
      board[i] = initialSeeds;
    
    if (log.isDebugEnabled())
      for (int i = 0; i <= (houses * 2); i++)
        log.debug("board[{}]: {}", i, board[i]);
  }
  
  /**
   * Return the number of seeds in one house
   * 
   * @param player what side of the board to look at
   * @param house the relative position of the pit to look at;
   *    starting from 1 and up to the number of houses per player
   *    
   * @return the number of seeds in the requested pit
   */
  public int getSeedCount(int player, int house) {
    return board[getArrayIndex(player, house)];
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }
  
  public void makeMove(int player, int house) {
    if (player != currentPlayer)
      return;
   
    int pickedSeeds = getSeedCount(player, house);
        
    if (pickedSeeds == 0)
      return;

    setSeedCount(player, house, 0);

    for (int i = 0; i < pickedSeeds; i++)
      addSeed(player, house + i + 1);

    switchPlayer();
  }
  
  private void addSeed(int player, int house) {
    int currentSeeds = getSeedCount(player, house);
    setSeedCount(player, house, currentSeeds + 1);
  }
  
  private void switchPlayer() {
    currentPlayer =
        currentPlayer == PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE;
  }
  
  /**
   * Set the number of seeds in one house. 
   * Made protected for for Unit Tests
   */
  protected void setSeedCount(int player, int house, int seeds) {
    board[getArrayIndex(player, house)] = seeds;
  }
  
  public int getStoreCount(int playerOne) {
    return 0;
  }
  
  public boolean isGameOver() {
    return false;
  }

  private int getArrayIndex(int player, int house) {
    if (house < 1 || house > houses)
      throw new IllegalArgumentException("house index is out of range");
    
    return (house - 1) + getOffsetForPlayer(player);
  }
  
  private int getOffsetForPlayer(int player) {
    if (player == PLAYER_ONE)
      return 0;
    
    // +1 = store for player one
    return (player * houses + 1);
  }
}
