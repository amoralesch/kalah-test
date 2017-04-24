package com.edaware.examples.kalah.service;

import com.edaware.examples.kalah.service.Board.Player;

public interface IBoard {
  /**
   * gets the total number of pits in the board.
   * pits are both the houses and the stores for both players
   *
   * @return number of pits
   */
  int getTotalPits();

  /**
   * return the number of seeds that were set in each house at the
   * begining of the game
   *
   * @return the number of seeds per house
   */
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

  /**
   * gets the player that has to make a move
   *
   * @return the player that will make the next move
   */
  Player getCurrentPlayer();

  /**
   * makes a move in the board. It checks if the move is valid and
   * perform all the moves in the pits
   *
   * @param player the player who made the move
   * @param house the player's house selected to make the move
   */
  void makeMove(Player player, int house);

  /**
   * Once the game is over, return the player with the biggest score.
   * If the game is a draw, returns null.
   *
   * @return the player who is the winner. Or Null if it is a draw
   */
  Player getWinner();

  /**
   * Return the current score for one player. This takes into account
   * both the players houses and the store
   *
   * @param player the player that we want to get the score
   * @return the total number of seeds for the player
   */
  int getScore(Player player);

  /**
   * Returns the number of seeds in the store for some player.
   *
   * @param player the owner of the store
   * @return the number of seeds in store
   */
  int getStoreCount(Player player);

  /**
   * Check to see if the game is over. A game is over when one player
   * doesn't have any more seeds in his/her houses
   *
   * @return TRUE: the game has ended; FALSE: players can still play
   */
  boolean isGameOver();
}