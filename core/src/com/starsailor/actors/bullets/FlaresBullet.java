package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.components.ComponentFactory;
import com.starsailor.data.WeaponProfile;
import com.starsailor.util.Resources;

import java.util.Random;

/**
 * Concrete implementation of a weapon type.
 */
public class FlaresBullet extends Bullet {
  public FlaresBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void createComponents(WeaponProfile weaponProfile) {
    super.createComponents(weaponProfile);

    //additional steering component
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponProfile.steeringData);
  }

  @Override
  public boolean create() {
    Body bb = bodyComponent.body;
    Random r = new Random();
    int angleDegree = r.nextInt((180 - (-180))) + (-180);

    float angle = (float) Math.toRadians(angleDegree);
    Vector2 force = new Vector2();
    force.x = (float) Math.cos(angle);
    force.y = (float) Math.sin(angle);
    force = force.scl(weaponProfile.forceFactor);

    bb.applyForceToCenter(force, true);
    bb.applyTorque(weaponProfile.torque, true);

    return true;
  }

  @Override
  public void update() {
    updateSpritePositionForBody(true);
    float angularVelocity = bodyComponent.body.getAngularVelocity();
    if(angularVelocity > 0 && angularVelocity < 0.5f) {
      markForDestroy();
    }
  }

  @Override
  public void collide(Ship ship, Vector2 position) {
    updateDamage(ship);
  }

  @Override
  public void collide(Bullet bullet, Vector2 position) {
    //ignore other flares
    if(bullet instanceof FlaresBullet) {
      return;
    }
    else {
      bullet.markForDestroy();
    }

    hitAndDestroyBullet(position, Resources.SOUND_EXPLOSION);
    markForDestroy();
  }
}
