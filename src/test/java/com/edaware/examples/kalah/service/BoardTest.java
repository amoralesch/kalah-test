package com.edaware.examples.kalah.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.edaware.examples.kalah.service.Board.Player;

public class BoardTest {
  private static final int DEFAULT_HOUSES = 3;
  
  private static final int DEFAULT_SEEDS = 2;
  
  Board board = new Board(DEFAULT_HOUSES, DEFAULT_SEEDS);
 
  @Test
  public void constructor_throwsException_whenSizeIsIncorrect()
      throws Exception {
    try {
      new Board(0);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }

    try {
      new Board(4, 0);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }
  }

  @Test
  public void constructor_createsSixHouseBoardWithFourSeeds_byDefault() 
      throws Exception 
  {
    Board board = new Board();
    
    assertThat(board.getTotalPits(), equalTo(14));
    assertThat(board.getInitialSeeds(), equalTo(4));
  }
  
  @Test
  public void constructor_createsNeededPits_always() throws Exception {
    Board b = new Board(4);
    
    assertThat(b.getTotalPits(), equalTo(10));
    assertThat(b.getInitialSeeds(), equalTo(4));
  }
  
  @Test
  public void constructor_setInitialSeeds_always() throws Exception {
    Board b = new Board(4, 2);

    assertThat(b.getTotalPits(), equalTo(10));
    assertThat(b.getInitialSeeds(), equalTo(2));
  }
  
  @Test
  public void constructor_generateCorrectSetup_always() throws Exception {
    for (int i = 1; i <= DEFAULT_HOUSES; i++)
      assertThat(board.getSeedCount(Player.PLAYER_ONE, i), equalTo(DEFAULT_SEEDS));
    
    for (int i = 1; i <= DEFAULT_HOUSES; i++)
      assertThat(board.getSeedCount(Player.PLAYER_TWO, i), equalTo(DEFAULT_SEEDS));

    assertThat(board.getStoreCount(Player.PLAYER_ONE), equalTo(0));
    assertThat(board.getStoreCount(Player.PLAYER_TWO), equalTo(0));
  }
  
  @Test
  public void getSeedCount_throwsException_whenIndexIsWrong()
      throws Exception {
    try {
      board.getSeedCount(Player.PLAYER_ONE, DEFAULT_HOUSES + 1);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }

    try {
      board.getSeedCount(Player.PLAYER_TWO, DEFAULT_HOUSES + 1);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }
  }

  @Test
  public void getNextPlayer_playerOne_whenStartingGame() throws Exception {
    assertThat(board.getCurrentPlayer(), equalTo(Player.PLAYER_ONE));
  }
  
  @Test
  public void makeMove_ignoresMove_whenIsNotPlayerTurn() throws Exception {
    board.makeMove(Player.PLAYER_TWO, 1);

    assertThat(board.getCurrentPlayer(), equalTo(Player.PLAYER_ONE));
    assertThat(board.getSeedCount(Player.PLAYER_TWO, 1), greaterThan(0));
  }

  @Test
  public void makeMove_ignoresMove_whenSelectedHouseIsEmpty() throws Exception {
    board.setSeedCount(Player.PLAYER_ONE, 1, 0);
    
    board.makeMove(Player.PLAYER_ONE, 1);

    assertThat(board.getCurrentPlayer(), equalTo(Player.PLAYER_ONE));
  }

  @Test
  public void makeMove_emptiesSelectedHouse_always() throws Exception {
    board.makeMove(Player.PLAYER_ONE, 1);

    assertThat(board.getSeedCount(Player.PLAYER_ONE, 1), equalTo(0));
  }
  
  @Test
  public void makeMove_addsSeedsNextHouses_always() throws Exception {
    board.makeMove(Player.PLAYER_ONE, 1);

    assertThat(board.getSeedCount(Player.PLAYER_ONE, 1), equalTo(0));
    assertThat(board.getSeedCount(Player.PLAYER_ONE, 2), equalTo(DEFAULT_SEEDS + 1));
    assertThat(board.getSeedCount(Player.PLAYER_ONE, 3), equalTo(DEFAULT_SEEDS + 1));
  }
  
  @Test
  public void makeMove_switchesPlayers_whenValidMove() throws Exception {
    assertThat(board.getCurrentPlayer(), equalTo(Player.PLAYER_ONE));
    
    board.makeMove(Player.PLAYER_ONE, 1);

    assertThat(board.getCurrentPlayer(), equalTo(Player.PLAYER_TWO));
  }
  
  @Test
  public void isGameOver_false_whenStonesAreBothPlayersHouses()
      throws Exception {
    // some stones are still there
    assertThat(board.getSeedCount(Player.PLAYER_ONE, 1), greaterThan(0));
    assertThat(board.getSeedCount(Player.PLAYER_TWO, 1), greaterThan(0));
    
    assertThat(board.isGameOver(), equalTo(false));
  }
}
