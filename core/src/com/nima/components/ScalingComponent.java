package com.nima.components;

import com.badlogic.ashley.core.Component;

/**
 * Component implementation for scaling
 */
public class ScalingComponent implements Component {
  private float upScaling = 0.01f;
  private float downScaling = 0.01f;

  private float currentScale;
  private float targetScale;

  public ScalingComponent(float scaling) {
    this.targetScale = scaling;
    this.currentScale = scaling;
  }

  public void updateScaling() {
    if(currentScale < targetScale) {
      currentScale+=upScaling;
    }
    else if(currentScale > targetScale) {
      currentScale-=downScaling;
    }

    currentScale = (float) (Math.round(currentScale * 100.0) / 100.0);
  }

  public float getCurrentScaling() {
    return currentScale;
  }

  public void setScaling(float scaling) {
    this.targetScale = scaling;
  }

  public float getScaling() {
    return currentScale;
  }
}
