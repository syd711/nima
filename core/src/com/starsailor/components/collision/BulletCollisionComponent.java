package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.managers.EntityManager;

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
