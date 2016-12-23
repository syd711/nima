package com.nima.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Common math function to be used.
 */
public class MathUtil {

  /**
   * Be aware to pass the screen coordinates here!
   */
  public static float getAngle(float sourceX, float sourceY, float targetX, float targetY) {
    return (float) Math.toDegrees(Math.atan2(targetY - sourceY, targetX - sourceX));
  }

  public static Vector2 getUpdatedCoordinates(float angle, float speed) {
    float absAngle = Math.abs(angle);
    float baseX = (float) (Math.cos(Math.toRadians(absAngle)) * speed);
    float baseY = (float) (Math.sin(Math.toRadians(absAngle)) * speed);
    return new Vector2(Math.abs(baseX), Math.abs(baseY));
  }
}
