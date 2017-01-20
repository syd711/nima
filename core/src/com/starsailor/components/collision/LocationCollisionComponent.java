package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Collidable;

/**
 * Collidable component for an ashley entity.
 */
public class LocationCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {

  }

  @Override
  public void reset() {

  }
}
