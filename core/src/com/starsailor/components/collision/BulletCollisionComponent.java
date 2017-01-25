package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.ParticleComponent;
import com.starsailor.components.ShipDataComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.*;
import com.starsailor.util.Resources;

/**
 * Collidable component for an ashley entity.
 */
public class BulletCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee, Vector2 position) {
    if(collidee instanceof NPC) {
      applyCollisionWith((Bullet) collider, (Ship) collidee, position);
    }
    else if(collider instanceof NPC) {
      applyCollisionWith((Bullet) collidee, (Ship) collider, position);
    }
  }

  public void applyCollisionWith(Bullet bullet, Ship npc, Vector2 position) {
    if(!bullet.isOwner(npc)) {
      applyImpactForce(bullet, npc, position);

      WeaponProfile.Types type = bullet.weaponProfile.type;
      switch(type) {
        case LASER: {
          hitAndDestroyBullet(bullet, position, Particles.EXPLOSION, Resources.SOUND_EXPLOSION);
          updateDamage(bullet, npc);
          break;
        }
        case MISSILE: {
          hitAndDestroyBullet(bullet, position, Particles.EXPLOSION, Resources.SOUND_EXPLOSION);
          updateDamage(bullet, npc);
          break;
        }
        case PHASER: {
          updateDamage(bullet, npc);
          break;
        }
      }
    }
  }

  /**
   * Checks if the ship is destroyed so that the selection
   * is resetted and an explosion animation is rendered.
   * @param bullet
   * @param npc
   */
  private void updateDamage(Bullet bullet, Ship npc) {
    ShipDataComponent shipData = npc.shipDataComponent;
    shipData.health = shipData.health-bullet.bulletDamageComponent.damage;
    if(shipData.health <= 0 ) {
      EntityManager.getInstance().destroy(npc);
      EntityManager.getInstance().destroy(bullet);

      ParticleComponent particleComponent = npc.particleComponent;
      if(particleComponent != null) {
        particleComponent.enabled = true;
      }

      SelectionManager.getInstance().setSelection(null);
    }
  }

  /**
   * Play sound, play effect and remove the entity from the system
   * @param bullet
   * @param effect
   * @param sound
   */
  private void hitAndDestroyBullet(Bullet bullet, Vector2 position, Particles effect, String sound) {
    EntityManager.getInstance().destroy(bullet);
    SoundManager.playSoundAtPosition(sound, 1f, new Vector3(position, 0));
    bullet.particleComponent.enabled = true;
  }

  /**
   * Apply force to the box2d so that the impact is visible
   * @param bullet
   * @param ship
   */
  private void applyImpactForce(Bullet bullet, Ship ship, Vector2 position) {
    BodyComponent component = ship.getComponent(BodyComponent.class);
    float impactFactor = bullet.weaponProfile.impactFactor;

    if(bullet.bodyComponent != null) {
      //use my linear velocity
      Vector2 linearVelocity = bullet.bodyComponent.body.getLinearVelocity();
      Vector2 force = new Vector2(linearVelocity.x*impactFactor, linearVelocity.y*impactFactor);
      //to apply it on the target
      component.body.applyForceToCenter(force.x, force.y, true);
    }
    else {
      //component.body.applyForceToCenter(force.x, force.y, true);
    }
  }

  @Override
  public void reset() {

  }
}
