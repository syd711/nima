package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.managers.CameraManager;
import com.starsailor.messaging.Messages;

/**
 * Collideable component for an ashley entity.
 */
public class BulletCollisionComponent implements Collidable, Pool.Poolable {

  private Bullet owner;

  public BulletCollisionComponent(Bullet owner) {
    this.owner = owner;
  }

  @Override
  public void handleCollision(Entity collidee, Vector2 position) {
    if(collidee instanceof Ship) {
      applyCollisionWith((Ship) collidee, position);
    }
    else if(collidee instanceof Bullet) {
      applyCollisionWith((Bullet) collidee, position);
    }
  }

  public void applyCollisionWith(Bullet bullet, Vector2 position) {
    //ignore bullets from the same entity
    if(bullet.owner.equals(owner)) {
      return;
    }

    bullet.collide(owner, position);
  }

  public void applyCollisionWith(Ship ship, Vector2 position) {
    if(!owner.isOwner(ship)) {
      owner.setActualHit(ship);
      owner.applyImpactForce(ship, position);
      owner.collide(ship, position);

      if(!owner.wasFriendlyFire()) {
        MessageManager.getInstance().dispatchMessage(Messages.ATTACK, owner);
      }

      if(ship instanceof Player) {
        CameraManager.getInstance().shake(owner.weaponData.getImpactFactor(), 80);
      }
    }
  }

  @Override
  public void reset() {
    owner = null;
  }
}
