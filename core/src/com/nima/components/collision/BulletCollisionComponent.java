package com.nima.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.nima.actors.Bullet;
import com.nima.actors.Collidable;
import com.nima.actors.NPC;
import com.nima.managers.EntityManager;

/**
 * Collidable component for an ashley entity.
 */
public class BulletCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {
    if(collidee instanceof NPC) {
      Bullet bullet = (Bullet) collider;
      if(!bullet.owner.equals(collidee)) {
        EntityManager.getInstance().destroy(collidee);
      }
    }
    else if(collider instanceof NPC) {
      Bullet bullet = (Bullet) collidee;
      if(!bullet.owner.equals(collider)) {
        EntityManager.getInstance().destroy(collider);
      }
    }
  }

  @Override
  public void reset() {

  }
}
