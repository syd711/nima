package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.Spine;

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
}
