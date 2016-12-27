package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.nima.actors.Spine;

/**
 * Component implementation for scaling
 */
public class ScalingComponent implements Component {
  private float upScaling = 0.1f;
  private float downScaling = 0.1f;

  private float currentScale;
  private float targetScale;

  private Spine spine;

  public ScalingComponent(Spine spine, float scaling) {
    this.targetScale = scaling;
    this.currentScale = scaling;
    this.spine = spine;
  }

  public void updateScaling() {
    if(currentScale < targetScale) {
      currentScale+=upScaling;
    }
    else if(currentScale > targetScale) {
      currentScale-=downScaling;
    }

    currentScale = (float) (Math.round(currentScale * 100.0) / 100.0);

    float scaleX = this.spine.skeleton.getRootBone().getScaleX();
    if(scaleX != currentScale) {
      this.spine.skeleton.getRootBone().setScale(currentScale);
    }
  }

  public void setScaling(float scaling) {
    this.targetScale = scaling;
  }

  public float getScaling() {
    return currentScale;
  }
}
