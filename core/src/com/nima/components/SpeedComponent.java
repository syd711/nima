package com.nima.components;

import com.badlogic.ashley.core.Component;

/**
 * The current speed of an entity
 */
public class SpeedComponent implements Component {

  public float maxSpeed;
  public float currentSpeed;
  public float targetSpeed;

  public SpeedComponent(float maxSpeed) {
    this.maxSpeed = maxSpeed;
    this.currentSpeed = 0;
  }

  public boolean isAtFullSpeed() {
    return this.targetSpeed == currentSpeed;
  }

  public void setTargetSpeedPercentage(float percentage) {
    System.out.println("Speed: " + percentage + "%");
    this.targetSpeed = maxSpeed*percentage/100;
  }

  public float getTargetSpeed() {
    return targetSpeed;
  }

  public float getCurrentSpeed() {
    return currentSpeed;
  }

  public float getMaxSpeed() {
    return maxSpeed;
  }

  public void setCurrentSpeed(float v) {
    if(v >=0) {
      this.currentSpeed = v;
    }
    if(currentSpeed > targetSpeed) {
      currentSpeed = targetSpeed;
    }
  }
}
