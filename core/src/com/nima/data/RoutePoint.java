package com.nima.data;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Model for a tracking point of a route.
 * This model is an entity although it is never added to Ashley.
 */
public class RoutePoint extends Entity {
  //steering defaults
  public float boundingRadius = 200;
  public float maxLinearSpeed = 500;
  public float maxLinearAcceleration = 500;
  public float maxAngularSpeed = 3000;
  public float maxAngularAcceleration = 300;
  
  public Vector2 position;
  public boolean dockable;
  public boolean spawnPoint;
  public Float dockTime;

  @Override
  public String toString() {
    return "Route Point " + position;
  }
}
