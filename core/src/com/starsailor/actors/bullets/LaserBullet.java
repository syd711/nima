package com.starsailor.actors.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.SoundManager;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.Resources;
import com.starsailor.util.Settings;

/**
 * Concrete implementation of a weapon type.
 */
public class LaserBullet extends Bullet {
  public LaserBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void create() {
    Vector2 from = Box2dUtil.toBox2Vector(owner.getCenter());
    Vector2 to = Box2dUtil.toBox2Vector(target.getCenter());
    float radianAngle = Box2dUtil.getBox2dAngle(from, to);

    Body bulletBody = bodyComponent.body;
    bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, radianAngle);

    float mXDir = -(float) Math.cos(radianAngle);
    float mYDir = -(float) Math.sin(radianAngle);

    float speedFactor = weaponProfile.forceFactor;
    Vector2 impulse = new Vector2(speedFactor * mXDir / Settings.PPM, speedFactor * mYDir / Settings.PPM);
    bulletBody.applyLinearImpulse(impulse, bulletBody.getPosition(), true);

    SoundManager.playSoundAtPosition(Resources.SOUND_LASER, 0.5f, new Vector3(owner.getCenter().x, owner.getCenter().y, 0));
  }

  @Override
  public void update() {
    updateSpritePositionForBody(true);
  }

  @Override
  protected void collide(Vector2 position) {

  }
}
