package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Collidable;

/**
 * Collidable component for an ashley entity.
 */
public class PlayerTargetCollisionComponent implements Collidable {
  @Override
  public void handleCollision(Entity collider, Entity collidee, Vector2 position) {
  }
}
