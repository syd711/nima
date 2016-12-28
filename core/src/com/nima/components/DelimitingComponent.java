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
  private float maxValue;

  private float decreaseBy;
  private float increaseBy;

  public DelimitingComponent(float targetValue, float currentValue, float maxValue, float decreaseBy, float increaseBy) {
    this.currentValue = currentValue;
    this.targetValue = targetValue;
    this.maxValue = maxValue;

    this.decreaseBy = decreaseBy;
    this.increaseBy = increaseBy;
  }

  public void updateValue() {
    if(currentValue != targetValue) {
      if(currentValue < targetValue) {
        currentValue += increaseBy;
      }
      else if(currentValue > targetValue) {
        currentValue -= decreaseBy;
      }

      currentValue = (float) (Math.round(currentValue * 100.0) / 100.0);
    }
  }

  public void setDecreaseBy(float value) {
    this.decreaseBy = value;
  }

  public void setIncreaseBy(float value) {
    this.increaseBy = value;
  }

  public float getCurrentValue() {
    return currentValue;
  }

  public void setTargetPercentage(float percentage) {
    float value = maxValue*percentage/100;
    targetValue = (float) (Math.round(value* 100.0) / 100.0);
  }

  public float getTargetValue() {
    return targetValue;
  }

  public void setTargetValue(float target) {
    this.targetValue = target;
  }
}
