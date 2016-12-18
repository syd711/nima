package com.nima.render;

import com.nima.actors.GameEntity;

/**
 * Interface to be implemented by entities that want
 * to listen on collision events.
 */
public interface CollisionListener {

  /**
   * Called when the object implementing this
   * interface collised with the given game entity.
   * @param entity the entity that intersects this entity
   */
  void collided(GameEntity entity);
}
