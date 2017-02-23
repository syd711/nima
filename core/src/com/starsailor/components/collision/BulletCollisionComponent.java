package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.managers.CameraManager;
import com.starsailor.messaging.Messages;

/**
 * Collideable component for an ashley entity.
 */
public class BulletCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee, Vector2 position) {
    if(!validEntities(collider, collidee)) {
      return;
    }

    if(collidee instanceof NPC) {
      applyCollisionWith((Bullet) collider, (Ship) collidee, position);
    }
    else if(collider instanceof NPC) {
      applyCollisionWith((Bullet) collidee, (Ship) collider, position);
    }
    else if(collider instanceof Bullet) {
      applyCollisionWith((Bullet) collidee, (Bullet) collider, position);
    }
    else if(collidee instanceof Bullet) {
      applyCollisionWith((Bullet) collider, (Bullet) collidee, position);
    }
  }

  public void applyCollisionWith(Bullet bullet, Bullet bullet2, Vector2 position) {
    //ignore bullets from the same entity
    if(bullet.owner.equals(bullet2.owner)) {
      return;
    }
    //TODO no particle effect yet
    bullet.collide(bullet2, position);
  }

  public void applyCollisionWith(Bullet bullet, Ship ship, Vector2 position) {
    if(!bullet.isOwner(ship)) {
      bullet.setActualHit(ship);
      bullet.applyImpactForce(ship, position);
      bullet.collide(ship, position);

      if(!bullet.wasFriendlyFire()) {
        MessageManager.getInstance().dispatchMessage(Messages.ATTACK, bullet);
      }

      if(ship instanceof Player) {
        CameraManager.getInstance().shake(bullet.weaponData.impactFactor, 80);
      }
    }
  }

  private boolean validEntities(Entity... entities) {
    for(Entity entity : entities) {
      if(entity instanceof NPC || entity instanceof Bullet) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void reset() {

  }
}
