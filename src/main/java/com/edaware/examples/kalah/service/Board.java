package com.edaware.examples.kalah.service;

public class Board {
  public Board(int houses) {
    if (houses <= 0)
      throw new IllegalArgumentException("invalid house number value");
  }
}
