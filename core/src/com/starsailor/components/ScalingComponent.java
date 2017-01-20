package com.starsailor.components;

/**
 * Component implementation for scaling
 */
public class ScalingComponent extends LimitingComponent {

  public void init(float scaling) {
    init(scaling,  scaling, 1f,0.01f, 0.01f);
  }
}
