package com.edaware.examples.kalah.service;

import com.edaware.examples.kalah.service.Board.Player;

public interface IBoard {
  int getTotalPits();

  int getInitialSeeds();

  /**
   * Return the number of seeds in one house
   *
   * @param player what side of the board to look at
   * @param house the relative position of the pit to look at;
   *    starting from 1 and up to the number of houses per player
   *
   * @return the number of seeds in the requested pit
   */
  int getSeedCount(Player player, int house);

  Player getCurrentPlayer();

  void makeMove(Player player, int house);

  Player getWinner();

  int getScore(Player player);

  int getStoreCount(Player player);

  boolean isGameOver();
}