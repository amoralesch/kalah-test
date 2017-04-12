package com.edaware.examples.kalah.service;

public class Board {
  public enum Player {
    FIRST,
    SECOND
  }
  
  private static final int DEFAULT_HOUSES = 6;
  
  private static final int DEFAULT_SEEDS = 4;

  private final int houses;
  
  private final int initialSeeds;
  
  private int[] board;
  
  private Player current = Player.FIRST;
  
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
  public int getSeedCount(Player player, int house) {
    return board[getIndexHouse(player, house)];
  }

  public Player getCurrentPlayer() {
    return current;
  }
  
  public void makeMove(Player player, int house) {
    if (player != current)
      return;
   
    int pickedSeeds = getSeedCount(player, house);
        
    if (pickedSeeds == 0)
      return;

    setSeedCount(player, house, 0);

    int thisHouseIndex = getIndexHouse(player, house);
    for (int i = 1; i <= pickedSeeds; i++)
      addSeed(thisHouseIndex + i);

    if ((thisHouseIndex + pickedSeeds) != getIndexStore(player))
      switchPlayer();
  }
  
  private void addSeed(int arrayIndex) {
    board[arrayIndex] = board[arrayIndex] + 1;
  }
  
  private void switchPlayer() {
    current = current == Player.FIRST ? 
        Player.SECOND : Player.FIRST;
  }
  
  /**
   * Set the number of seeds in one house. 
   * Made protected for for Unit Tests
   */
  protected void setSeedCount(Player player, int house, int seeds) {
    board[getIndexHouse(player, house)] = seeds;
  }
  
  public int getStoreCount(Player player) {
    return board[getIndexStore(player)];
  }
  
  public boolean isGameOver() {
    return false;
  }

  private int getIndexHouse(Player player, int house) {
    if (house < 1 || house > houses)
      throw new IllegalArgumentException("house index is out of range");
    
    return (house - 1) + getOffsetForPlayer(player);
  }
  
  private int getIndexStore(Player player) {
    return player == Player.FIRST ?
        houses : (houses * 2) + 1;
  }
  
  private int getOffsetForPlayer(Player player) {
    if (player == Player.FIRST)
      return 0;
    
    // +1 = store for player one
    return houses + 1;
  }
}
