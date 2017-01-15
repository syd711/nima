package com.nima.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.nima.actors.Collidable;

/**
 * Collidable component for an ashley entity.
 */
public class PlayerCollisionComponent implements Component, Collidable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {

  }
}
