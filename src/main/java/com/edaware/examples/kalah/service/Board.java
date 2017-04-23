package com.edaware.examples.kalah.service;

public class Board implements IBoard {
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

    for (int i = 0; i < houses; i++) {
      board[i] = initialSeeds;
      board[i + houses + 1] = initialSeeds;
    }
  }

  @Override
  public int getTotalPits() {
    return board.length;
  }

  @Override
  public int getInitialSeeds() {
    return initialSeeds;
  }

  @Override
  public int getSeedCount(Player player, int house) {
    return board[getIndexHouse(player, house)];
  }

  @Override
  public Player getCurrentPlayer() {
    return current;
  }

  @Override
  public void makeMove(Player player, int house) {
    if (!isValidMove(player, house))
      return;

    int seeds = takeAllSeeds(player, house);

    Player houseOwner = player;
    int nextHouse = house + 1;
    boolean seedInStore = false;
    boolean stealSeeds = false;

    while (seeds > 0) {
      seedInStore = nextHouse > houses;

      if (seedInStore) {
        if (houseOwner == player) {
          addSeedsStore(houseOwner, 1);
          seeds--;
        }

        nextHouse = 1;
        houseOwner = getEnemy(houseOwner);
      } else {
        if (seeds == 1)
          stealSeeds = (getSeedCount(houseOwner, nextHouse) == 0)
              && (houseOwner == player);

        addSeedsPlayer(houseOwner, nextHouse, 1);
        nextHouse++;
        seeds--;
      }
    }

    if (stealSeeds) {
      addSeedsStore(player, takeAllSeeds(
          getEnemy(player),
          getComplementHouse(nextHouse - 1)));
    }

    if (!seedInStore)
      switchPlayer();
  }

  @Override
  public Player getWinner() {
    if (!isGameOver())
      throw new IllegalStateException("game has not ended");

    int pointsOne = getScore(Player.FIRST);
    int pointsTwo = getScore(Player.SECOND);

    // draw
    if (pointsOne == pointsTwo)
      return null;

    return pointsOne > pointsTwo ? Player.FIRST : Player.SECOND;
  }

  @Override
  public int getScore(Player player) {
    int points = getStoreCount(player);

    for (int i = 1; i <= houses; i++)
      points += getSeedCount(player, i);

    return points;
  }

  private int getComplementHouse(int house) {
    return (houses + 1) - house;
  }

  private int takeAllSeeds(Player player, int house) {
    int index = getIndexHouse(player, house);
    int seeds = board[index];
    board[index] = 0;
    return seeds;
  }

  private boolean isValidMove(Player player, int house) {
    if (isGameOver())
      return false;

    if (player != current)
      return false;

    return getSeedCount(player, house) > 0;
  }

  private void addSeedsPlayer(Player player, int house, int seeds) {
    board[getIndexHouse(player, house)] += seeds;
  }

  /**
   * Adds some seeds to the player's store pit
   *
   * @param player the player owner of the store
   * @param seeds the number of seeds to add
   */
  private void addSeedsStore(Player player, int seeds) {
    board[getIndexStore(player)] += seeds;
  }

  private void switchPlayer() {
    current = getEnemy(current);
  }

  private Player getEnemy(Player p) {
    return p == Player.FIRST ? Player.SECOND : Player.FIRST;
  }

  /**
   * Set the number of seeds in one house.
   * Made protected for Unit Tests
   */
  protected void setSeedCount(Player player, int house, int seeds) {
    board[getIndexHouse(player, house)] = seeds;
  }

  /**
   * Set the number of seeds in one store.
   * Made protected for Unit Tests
   */
  protected void setStoreSeeds(Player player, int seeds) {
    board[getIndexStore(player)] = seeds;
  }

  @Override
  public int getStoreCount(Player player) {
    return board[getIndexStore(player)];
  }

  @Override
  public boolean isGameOver() {
    boolean playerOneHasStones = false;
    boolean playerTwoHasStones = false;

    for (int i = 1; i <= houses; i++) {
      if (getSeedCount(Player.FIRST, i) > 0)
        playerOneHasStones = true;

      if (getSeedCount(Player.SECOND, i) > 0)
        playerTwoHasStones = true;

      if (playerOneHasStones && playerTwoHasStones)
        return false;
    }

    return true;
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
