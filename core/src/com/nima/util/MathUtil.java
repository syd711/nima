package com.nima.util;

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
}
