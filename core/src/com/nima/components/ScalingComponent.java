package com.nima.components;

import com.badlogic.ashley.core.Component;

/**
 * Component implementation for scaling
 */
public class ScalingComponent implements Component {
  //default is full size, this is a different scaling the initial loading one
  private float scaling = 1f;
  private float currentScale;
  private float targetScale;

  private SpineComponent spineComponent;

  public ScalingComponent(SpineComponent spineComponent, float scaling) {
    this.scaling = scaling;
    this.spineComponent = spineComponent;
  }

  public void setScaling(float scaling) {
    this.scaling = scaling;
    this.spineComponent.skeleton.getRootBone().setScale(scaling);
  }
}
