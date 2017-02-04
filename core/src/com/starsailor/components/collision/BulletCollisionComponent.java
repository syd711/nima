package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.components.BodyComponent;
import com.starsailor.managers.CameraManager;

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
    bullet.collide(bullet2, position);
  }

  public void applyCollisionWith(Bullet bullet, Ship ship, Vector2 position) {
    if(!bullet.isOwner(ship)) {
      applyImpactForce(bullet, ship, position);
      bullet.collide(ship, position);

      if(ship instanceof Player) {
        CameraManager.getInstance().shake(bullet.weaponProfile.impactFactor, 80);
      }
    }
  }

  /**
   * Apply force to the box2d so that the impact is visible
   */
  private void applyImpactForce(Bullet bullet, Ship ship, Vector2 position) {
    BodyComponent component = ship.getComponent(BodyComponent.class);
    float impactFactor = bullet.weaponProfile.impactFactor;

    if(bullet.bodyComponent != null) {
      //use my linear velocity
      Vector2 linearVelocity = bullet.bodyComponent.body.getLinearVelocity();
      Vector2 force = new Vector2(linearVelocity.x * impactFactor, linearVelocity.y * impactFactor);
      //to apply it on the target
      component.body.applyForceToCenter(force.x, force.y, true);
    }
    else {
      //TODO e.g. phaser
      //component.body.applyForceToCenter(force.x, force.y, true);
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
