package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.ComponentFactory;
import com.starsailor.data.SteeringData;

/**
 * Model for a tracking point of a route.
 * This model is an entity although it is never added to Ashley.
 */
public class RoutePoint extends GameEntity {
  //steering defaults
  public SteeringData steeringData = new SteeringData();
  
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
