package com.nima.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Model for a tracking point of a route
 */
public class RoutePoint {
  //steering defaults
  public float boundingRadius = 200;
  public float maxLinearSpeed = 500;
  public float maxLinearAcceleration = 500;
  public float maxAngularSpeed = 3000;
  public float maxAngularAcceleration = 300;
  
  public Vector2 position;
  public boolean dockable;
  public boolean spawnPoint;
  public Integer dockTime;

  @Override
  public String toString() {
    return "Route Point " + position;
  }
}
