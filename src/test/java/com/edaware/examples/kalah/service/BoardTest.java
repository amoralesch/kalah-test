package com.edaware.examples.kalah.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.edaware.examples.kalah.service.Board.Player;

public class BoardTest {
  private static final int HOUSES = 3;

  private static final int SEEDS = 2;

  Board board = new Board(HOUSES, SEEDS);

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
    for (int i = 1; i <= HOUSES; i++) {
      assertThat(board.getSeedCount(Player.FIRST, i), equalTo(SEEDS));
      assertThat(board.getSeedCount(Player.SECOND, i), equalTo(SEEDS));
    }

    assertThat(board.getStoreCount(Player.FIRST), equalTo(0));
    assertThat(board.getStoreCount(Player.SECOND), equalTo(0));
  }

  @Test
  public void getSeedCount_throwsException_whenIndexIsWrong()
      throws Exception {
    try {
      board.getSeedCount(Player.FIRST, HOUSES + 1);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }

    try {
      board.getSeedCount(Player.SECOND, HOUSES + 1);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }
  }

  @Test
  public void getNextPlayer_playerOne_whenStartingGame() throws Exception {
    assertThat(board.getCurrentPlayer(), equalTo(Player.FIRST));
  }

  @Test
  public void makeMove_ignoresMove_whenIsNotPlayerTurn() throws Exception {
    board.makeMove(Player.SECOND, 1);

    assertThat(board.getCurrentPlayer(), equalTo(Player.FIRST));
    assertThat(board.getSeedCount(Player.SECOND, 1), greaterThan(0));
  }

  @Test
  public void makeMove_ignoresMove_whenSelectedHouseIsEmpty() throws Exception {
    board.setSeedCount(Player.FIRST, 1, 0);

    board.makeMove(Player.FIRST, 1);

    assertThat(board.getCurrentPlayer(), equalTo(Player.FIRST));
  }

  @Test
  public void makeMove_ignoreMove_whenGameAlreadyFinished() throws Exception {
    for (int i = 1; i <= HOUSES; i++)
      board.setSeedCount(Player.SECOND, i, 0);

    assertThat(board.isGameOver(), equalTo(true));

    assertThat(board.getSeedCount(Player.FIRST, 1), equalTo(SEEDS));
    board.makeMove(Player.FIRST, 1);

    assertThat(board.getSeedCount(Player.FIRST, 1), equalTo(SEEDS));
    assertThat(board.getCurrentPlayer(), equalTo(Player.FIRST));
  }

  @Test
  public void makeMove_emptiesSelectedHouse_always() throws Exception {
    board.makeMove(Player.FIRST, 1);

    assertThat(board.getSeedCount(Player.FIRST, 1), equalTo(0));
  }

  @Test
  public void makeMove_addsSeedsNextHouses_always() throws Exception {
    board.makeMove(Player.FIRST, 1);

    assertThat(board.getSeedCount(Player.FIRST, 1), equalTo(0));
    assertThat(board.getSeedCount(Player.FIRST, 2), equalTo(SEEDS + 1));
    assertThat(board.getSeedCount(Player.FIRST, 3), equalTo(SEEDS + 1));
  }

  @Test
  public void makeMove_dontAddStone_inEnemyStore() throws Exception {
    board.setSeedCount(Player.FIRST, HOUSES, HOUSES + 2);

    board.makeMove(Player.FIRST, HOUSES);

    assertThat(board.getSeedCount(Player.FIRST, HOUSES), equalTo(0));
    assertThat(board.getStoreCount(Player.FIRST), equalTo(1));

    assertThat(board.getSeedCount(Player.SECOND, 1), equalTo(SEEDS + 1));
    assertThat(board.getSeedCount(Player.SECOND, 2), equalTo(SEEDS + 1));
    assertThat(board.getSeedCount(Player.SECOND, 3), equalTo(SEEDS + 1));
    assertThat(board.getStoreCount(Player.SECOND), equalTo(0));

    assertThat(board.getSeedCount(Player.FIRST, 1), equalTo(SEEDS + 1));
  }

  @Test
  public void makeMove_stealEnemySeeds_whenLastSeedIsInOwnEmptyHouse()
      throws Exception {
    board.setSeedCount(Player.FIRST, HOUSES, 0);

    assertThat(board.getSeedCount(Player.SECOND, 1), equalTo(SEEDS));

    board.makeMove(Player.FIRST, 1);

    assertThat(board.getSeedCount(Player.FIRST, 1), equalTo(0));
    assertThat(board.getSeedCount(Player.FIRST, 2), equalTo(SEEDS + 1));
    assertThat(board.getSeedCount(Player.FIRST, 3), equalTo(1));

    assertThat(board.getStoreCount(Player.FIRST), equalTo(SEEDS));

    assertThat(board.getSeedCount(Player.SECOND, 1), equalTo(0));
  }

  @Test
  public void makeMove_dontStealOwnSeeds_whenLastSeedIsInEnemyEmptyHouse()
      throws Exception {
    board.setSeedCount(Player.FIRST, 2, 3);
    board.setSeedCount(Player.SECOND, 1, 0);

    assertThat(board.getSeedCount(Player.FIRST, HOUSES), equalTo(SEEDS));

    board.makeMove(Player.FIRST, 2);

    assertThat(board.getSeedCount(Player.FIRST, 2), equalTo(0));
    assertThat(board.getSeedCount(Player.FIRST, 3), equalTo(SEEDS + 1));

    assertThat(board.getStoreCount(Player.FIRST), equalTo(1));

    assertThat(board.getSeedCount(Player.SECOND, 1), equalTo(1));
    assertThat(board.getStoreCount(Player.SECOND), equalTo(0));
  }

  @Test
  public void makeMove_playerGetSecondMove_whenLastStoneIsInStore()
      throws Exception {
    board.makeMove(Player.FIRST, 2);

    assertThat(board.getSeedCount(Player.FIRST, 2), equalTo(0));
    assertThat(board.getSeedCount(Player.FIRST, 3), equalTo(SEEDS + 1));
    assertThat(board.getStoreCount(Player.FIRST), equalTo(1));

    assertThat(board.getCurrentPlayer(), equalTo(Player.FIRST));
  }

  @Test
  public void makeMove_switchesPlayers_whenValidMove() throws Exception {
    assertThat(board.getCurrentPlayer(), equalTo(Player.FIRST));

    board.makeMove(Player.FIRST, 1);

    assertThat(board.getCurrentPlayer(), equalTo(Player.SECOND));
  }

  @Test
  public void makeMove_addStonesToEnemy_whenTooMuchStones() throws Exception {
    board.makeMove(Player.FIRST, 3);

    assertThat(board.getSeedCount(Player.FIRST, 3), equalTo(0));
    assertThat(board.getStoreCount(Player.FIRST), equalTo(1));

    assertThat(board.getSeedCount(Player.SECOND, 1), equalTo(SEEDS + 1));

    assertThat(board.getCurrentPlayer(), equalTo(Player.SECOND));
  }

  @Test
  public void isGameOver_false_whenStonesAreBothPlayersHouses()
      throws Exception {
    // some stones are still there
    assertThat(board.getSeedCount(Player.FIRST, 1), greaterThan(0));
    assertThat(board.getSeedCount(Player.SECOND, 1), greaterThan(0));

    assertThat(board.isGameOver(), equalTo(false));
  }

  @Test
  public void isGameOver_true_whenOwnPitsAreEmpty() throws Exception {
    for (int i = 1; i <= HOUSES; i++)
      board.setSeedCount(Player.FIRST, i, 0);

    assertThat(board.isGameOver(), equalTo(true));
  }
}
