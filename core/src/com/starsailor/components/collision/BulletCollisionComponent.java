package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;

/**
 * Collidable component for an ashley entity.
 */
public class BulletCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee, Vector2 position) {
    if(collidee instanceof NPC) {
      ((Bullet)collider).applyCollisionWith((Ship) collidee, position);
    }
    else if(collider instanceof NPC) {
      ((Bullet)collidee).applyCollisionWith((Ship) collider, position);
    }
  }

  @Override
  public void reset() {

  }
}
