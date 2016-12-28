package com.nima.components;

/**
 * Component implementation for scaling
 */
public class ScalingComponent extends DelimitingComponent {

  public ScalingComponent(float scaling) {
    super(scaling,  scaling, 1f,0.01f, 0.01f);
  }
}
