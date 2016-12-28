package com.nima.components;

import com.badlogic.ashley.core.Component;

/**
 * Reusable component for a component that has upper and lower bounds
 * with an incrementing and decrementing value.
 * The lower bound is assumed to be always 0.
 */
abstract public class DelimitingComponent implements Component {

  private float targetValue;
  private float currentValue;

  private float decreaseBy;
  private float increaseBy;

  public DelimitingComponent(float value, float decreaseBy, float increaseBy) {
    this.currentValue = value;
    this.targetValue = value;
    this.decreaseBy = decreaseBy;
    this.increaseBy = increaseBy;
  }

  public void updateValue() {
    if(currentValue < targetValue) {
      currentValue += increaseBy;
    }
    else if(currentValue > targetValue) {
      currentValue -= decreaseBy;
    }

    currentValue = (float) (Math.round(currentValue * 100.0) / 100.0);
  }

  public float getCurrentVaule() {
    return currentValue;
  }

  public void setTargetValue(float target) {
    this.targetValue = target;
  }
}
