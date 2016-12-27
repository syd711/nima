package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.Spine;

/**
 * Component implementation for scaling
 */
public class ScalingComponent implements Component {
  //default is full size, this is a different scaling the initial loading one
  private float scaling = 1f;
  private float currentScale;
  private float targetScale;

  private Spine spine;

  public ScalingComponent(Spine spine, float scaling) {
    this.scaling = scaling;
    this.spine = spine;
  }

  public void setScaling(float scaling) {
    this.scaling = scaling;
    this.spine.skeleton.getRootBone().setScale(scaling);
  }

  public float getScaling() {
    return scaling;
  }
}
