package com.starsailor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.render.TmxSettings;

/**
 * Common math function to be used.
 */
public class GraphicsUtil {

  public static float getAngle(float sourceX, float sourceY, float targetX, float targetY) {
    return (float) Math.toDegrees(Math.atan2(targetY - sourceY, targetX - sourceX));
  }

  public static float getAngle(Vector2 source, Vector2 target) {
    return getAngle(source.x, source.y, target.x, target.y);
  }

  public static float vectorToAngle(Vector2 vector2) {
    return (float) Math.atan2(-vector2.x, vector2.y);
  }

  public static Vector2 angleToVector(Vector2 outVector, float angle) {
    outVector.x = -(float) Math.sin(angle);
    outVector.y = (float) Math.cos(angle);
    return outVector;
  }

  public static Vector2 getScreenCenter(float objectHeight) {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float targetX = TmxSettings.START_FRAME_X * TmxSettings.FRAME_PIXELS_X + (w / 2);
    float targetY = TmxSettings.START_FRAME_Y * TmxSettings.FRAME_PIXELS_Y + (h / 2) + objectHeight / 2;
    return new Vector2(targetX, targetY);
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
