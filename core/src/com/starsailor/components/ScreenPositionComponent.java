package com.starsailor.components;

import com.badlogic.ashley.core.Component;

/**
 *
 */
public class ScreenPositionComponent implements Component {
  private float x = 0.0f;
  private float y = 0.0f;

  private float defaultX;
  private float defaultY;

  public ScreenPositionComponent(float targetX, float targetY) {
    this.x = targetX;
    this.y = targetY;

    this.defaultX = targetX;
    this.defaultY = targetY;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getDefaultX() {
    return defaultX;
  }

  public float getDefaultY() {
    return defaultY;
  }
}
