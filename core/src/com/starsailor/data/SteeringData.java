package com.starsailor.data;

/**
 * All attributes required for a steering component
 */
public class SteeringData {
  public float boundingRadius = 200;
  public float maxLinearSpeed = 500;
  public float maxLinearAcceleration = 500;
  public float maxAngularSpeed = 3000;
  public float maxAngularAcceleration = 300;

  //custom value
  public float maxAngularChange = 0;
}
