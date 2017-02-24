package com.starsailor.data;

import com.google.gson.annotations.Expose;

/**
 * All attributes required for a steering component
 */
public class SteeringData extends GameData {
  @Expose
  private float boundingRadius = 200;
  @Expose
  private float maxLinearSpeed = 5;
  @Expose
  private float maxLinearAcceleration = 5;
  @Expose
  private float maxAngularSpeed = 1;
  @Expose
  private float maxAngularAcceleration = 1;

  public float getBoundingRadius() {
    return boundingRadius;
  }

  public void setBoundingRadius(float boundingRadius) {
    this.boundingRadius = boundingRadius;
  }

  public float getMaxLinearSpeed() {
    return maxLinearSpeed;
  }

  public void setMaxLinearSpeed(float maxLinearSpeed) {
    this.maxLinearSpeed = maxLinearSpeed;
  }

  public float getMaxLinearAcceleration() {
    return maxLinearAcceleration;
  }

  public void setMaxLinearAcceleration(float maxLinearAcceleration) {
    this.maxLinearAcceleration = maxLinearAcceleration;
  }

  public float getMaxAngularSpeed() {
    return maxAngularSpeed;
  }

  public void setMaxAngularSpeed(float maxAngularSpeed) {
    this.maxAngularSpeed = maxAngularSpeed;
  }

  public float getMaxAngularAcceleration() {
    return maxAngularAcceleration;
  }

  public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    this.maxAngularAcceleration = maxAngularAcceleration;
  }

  @Override
  public String toString() {
    return "Steering Data";
  }
}
