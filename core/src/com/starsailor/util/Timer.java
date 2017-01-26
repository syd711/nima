package com.starsailor.util;

/**
 * Utility class for timings
 */
public class Timer {

  private long duration;
  private long startTime;

  public Timer(long duration, long currentGameTime) {
    this.duration = duration;
    this.startTime = currentGameTime;
  }

  public boolean isExpired() {
    return startTime > duration;
  }

  public void reset(long currentGameTime) {
    startTime = currentGameTime;
  }

  /**
   * To be updated with the gaming time, not the real time
   * @param currentGameTime amount of millies the user is in the game
   */
  public void update(long currentGameTime) {
    this.startTime+=(currentGameTime-startTime);
  }
}
