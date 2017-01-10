package com.nima.data;

/**
 * Contains all attributes a ship can have.
 */
public class ShipProfile {
  public String spine;
  public float scale;

  //Speed
  public float maxSpeed;
  public float rotationSpeed;
  public float increaseSpeed;
  public float decreaseSpeed;

  //Steering
  public float boundingRadius;
  public float maxLinearSpeed;
  public float maxLinearAcceleration;
  public float maxAngularSpeed;
  public float maxAngularAcceleration;
}
