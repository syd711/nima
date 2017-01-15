package com.nima.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.nima.actors.Collidable;

/**
 * Collidable component for an ashley entity.
 */
public class LocationCollisionComponent implements Component, Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {

  }

  @Override
  public void reset() {

  }
}
