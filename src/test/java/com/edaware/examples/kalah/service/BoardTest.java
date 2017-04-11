package com.edaware.examples.kalah.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BoardTest {
  @Test
  public void constructor_throwsException_whenSizeIsIncorrect()
      throws Exception {
    try {
      new Board(0);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException ex) {
      // success
    }
  }

  @Test
  public void constructor_createsValidBoard_always() throws Exception {
    Board b = new Board(4);
    
    assertThat(b.getTotalPits(), equalTo(10));
  }
}
