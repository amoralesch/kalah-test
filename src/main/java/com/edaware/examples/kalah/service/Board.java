package com.edaware.examples.kalah.service;

public class Board {
  private static final int DEFAULT_HOUSES = 6;
  
  private static final int DEFAULT_SEEDS = 4;
  
  private final int houses;
  
  private final int initSeeds;
  
  public Board() {
    this(DEFAULT_HOUSES);
  }
  
  public Board(int houses) {
    this(houses, DEFAULT_SEEDS);
  }
  
  public Board(int houses, int seeds) {
    if (houses <= 0 || seeds <= 0)
      throw new IllegalArgumentException("invalid house number value");
    
    this.houses = houses;
    initSeeds = seeds;
  }
  
  public int getTotalPits() {
    // each player gets _houses_ pits (times 2)
    // plus 1 store each (plus 2)
    
    return (houses * 2) + 2;
  }
  
  public int getInitialSeeds() {
    return initSeeds;
  }
}
