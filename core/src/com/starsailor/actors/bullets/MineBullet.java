package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponData;
import com.starsailor.util.box2d.Box2dUtil;
import com.starsailor.util.Resources;

/**
 * Concrete implementation of a weapon type.
 */
public class MineBullet extends Bullet {
  public MineBullet(WeaponData weaponData, Ship owner, Ship target) {
    super(weaponData, owner, target);
  }

  @Override
  public boolean create() {
    //apply initial force to the mine
    Body bulletBody = bodyComponent.body;
    Body ownerBody = owner.bodyComponent.body;
    if(ownerBody == null) {
      return false;
    }

    bulletBody.setTransform(bulletBody.getPosition(), ownerBody.getAngle());

    float angle = ownerBody.getAngle();
    angle = Box2dUtil.addDegree(angle, -90);
    Vector2 force = new Vector2();
    force.x = (float) Math.cos(angle);
    force.y = (float) Math.sin(angle);
    force = force.scl(weaponData.getForceFactor());

    bulletBody.applyForceToCenter(force, true);
    bulletBody.applyTorque(0.1f, true);

    return false;
  }

  @Override
  public void update() {
    updateSpritePositionForBody(true);
  }

  @Override
  public void collide(Ship ship, Vector2 position) {
    hitAndDestroyBullet(position, Resources.SOUND_EXPLOSION);
    updateDamage(ship);
  }

  @Override
  public void collide(Bullet bullet, Vector2 position) {

  }
}
