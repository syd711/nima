package com.starsailor.util;

import com.badlogic.gdx.ai.GdxAI;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for timings
 */
public class GameTimer {

  private float duration;
  private float deltaTime = 0;

  private static List<GameTimer> timers = new ArrayList<>();

  public GameTimer(float duration) {
    this.duration = duration;
    timers.add(this);
  }

  public boolean isExpired() {
    if(deltaTime * 1000 > duration) {
      timers.remove(this);
      return true;
    }
    return false;
  }

  public void reset() {
    deltaTime = 0;
    timers.add(this);
  }

  public float getDeltaTimeMillis() {
    return deltaTime * 1000;
  }

  /**
   * To be updated with the gaming time, not the real time
   */
  public static float update(float deltaTime) {
    for(GameTimer timer : timers) {
      timer.updateDeltaTime(deltaTime);
    }

    GdxAI.getTimepiece().update(deltaTime);
    return GdxAI.getTimepiece().getDeltaTime();
  }

  /**
   * Use the delta time to update the timer;
   *
   * @param deltaTime
   */
  private void updateDeltaTime(float deltaTime) {
    this.deltaTime += deltaTime;
  }
}
