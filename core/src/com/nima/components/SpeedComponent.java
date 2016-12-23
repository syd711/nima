package com.nima.components;

import com.badlogic.ashley.core.Component;

/**
 * The current speed of an entity
 */
public class SpeedComponent implements Component {

  public float currentValue;
  public float targetValue;

  public SpeedComponent(float value) {
    this.targetValue = value;
    this.currentValue = 0;
  }

  public boolean isAtFullSpeed() {
    return this.targetValue == currentValue;
  }

  public void setCurrentValue(float v) {
    if(v >=0) {
      this.currentValue = v;
    }
    if(currentValue > targetValue) {
      currentValue = targetValue;
    }
  }
}
