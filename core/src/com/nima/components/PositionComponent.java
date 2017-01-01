package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class PositionComponent implements Component {
  public float x = 0.0f;
  public float y = 0.0f;

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vector2 getPosition() {
    return new Vector2(x, y);
  }
}
