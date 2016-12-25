package com.nima.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Common math function to be used.
 */
public class GraphicsUtil {

  public static float getAngle(float sourceX, float sourceY, float targetX, float targetY) {
    return (float) Math.toDegrees(Math.atan2(targetY - sourceY, targetX - sourceX));
  }

  public static Vector2 getUpdatedCoordinates(float angle, float speed) {
    float absAngle = Math.abs(angle);
    float baseX = (float) (Math.cos(Math.toRadians(absAngle)) * speed);
    float baseY = (float) (Math.sin(Math.toRadians(absAngle)) * speed);
    return new Vector2(Math.abs(baseX), Math.abs(baseY));
  }

  public static Vector2 transform2WorldCoordinates(OrthographicCamera camera, float screenX, float screenY) {
    float targetX = camera.position.x;
    float targetY = camera.position.y;

    float centerX = Gdx.graphics.getWidth() / 2;
    float centerY = Gdx.graphics.getHeight() / 2;

    if(screenX < centerX) {
      targetX = targetX - (centerX - screenX);
    }
    else {
      targetX = targetX + screenX - centerX;
    }

    if(screenY < centerY) {
      targetY = targetY - (centerY - screenY);
    }
    else {
      targetY = targetY + screenY - centerY;
    }

    return new Vector2(targetX, targetY);
  }
}
