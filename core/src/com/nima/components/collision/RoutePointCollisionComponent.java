package com.nima.components.collision;

import com.badlogic.ashley.core.Entity;
import com.nima.actors.Collidable;

/**
 * Collidable component for an ashley entity.
 */
public class RoutePointCollisionComponent implements Collidable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {
  }
}
