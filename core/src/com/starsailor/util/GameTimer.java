package com.starsailor.util;

import com.badlogic.gdx.ai.GdxAI;

/**
 * Utility class for timings
 */
public class GameTimer {

  private long duration;
  private long startTime;

  public GameTimer(long duration, long currentGameTime) {
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
   */
  public static float update(float deltaTime) {
    GdxAI.getTimepiece().update(deltaTime);
    return GdxAI.getTimepiece().getDeltaTime();
  }
}
