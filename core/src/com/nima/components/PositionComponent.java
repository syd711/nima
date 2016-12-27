package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.Spine;

/**
 *
 */
public class PositionComponent implements Component {
  public float x = 0.0f;
  public float y = 0.0f;

  private Spine spine;

  public PositionComponent(Spine spine) {
    this.spine = spine;
  }

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
    if(this.spine != null) {
      this.spine.skeleton.setPosition(x ,y);
    }
  }
}
