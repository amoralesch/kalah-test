package com.edaware.examples.kalah.service;

public class Board {
  private final int houses;
  
  public Board(int houses) {
    if (houses <= 0)
      throw new IllegalArgumentException("invalid house number value");
    
    this.houses = houses;
  }
  
  public int getTotalPits() {
    // each player gets _houses_ pits (times 2)
    // plus 1 store each (plus 2)
    
    return (houses * 2) + 2;
  }
}
