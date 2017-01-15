package com.nima.actors;

import com.badlogic.gdx.math.Vector2;
import com.nima.components.ComponentFactory;

/**
 * Model for a tracking point of a route.
 * This model is an entity although it is never added to Ashley.
 */
public class RoutePoint extends AshleyEntity {
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

  public RoutePoint() {
    ComponentFactory.addRoutePointCollisionComponent(this);
  }

  @Override
  public String toString() {
    return "Route Point " + position;
  }
}
