package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponProfile;
import com.starsailor.util.Box2dUtil;

/**
 * Concrete implementation of a weapon type.
 */
public class MineBullet extends Bullet {
  public MineBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void create() {
    //apply initial force to the mine
    Body bulletBody = bodyComponent.body;
    Body ownerBody = owner.bodyComponent.body;
    bulletBody.setTransform(bulletBody.getPosition(), ownerBody.getAngle());

    float angle = ownerBody.getAngle();
    angle = Box2dUtil.addDegree(angle, -90);
    Vector2 force = new Vector2();
    force.x = (float) Math.cos(angle);
    force.y = (float) Math.sin(angle);
    force = force.scl(weaponProfile.forceFactor);

    bulletBody.applyForceToCenter(force, true);
    bulletBody.applyTorque(0.1f, true);
  }

  @Override
  public void update() {
    updateSpritePositionForBody(true);
  }

  @Override
  protected void collide(Vector2 position) {

  }
}
