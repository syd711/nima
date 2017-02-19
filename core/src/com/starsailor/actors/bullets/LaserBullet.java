package com.starsailor.actors.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.components.SpriteComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.util.box2d.Box2dUtil;
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
  public boolean create() {
    Vector2 from = Box2dUtil.toBox2Vector(owner.getCenter());
    Vector2 to = Box2dUtil.toBox2Vector(target.getCenter());
    float radianAngle = Box2dUtil.getBox2dAngle(from, to);

    SpriteComponent.SpriteItem spriteItem = getSpriteItem();
    Sprite bulletSprite = spriteItem.getSprite();
    bulletSprite.setRotation((float) Math.toDegrees(radianAngle));

    Body bulletBody = bodyComponent.body;
    bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, radianAngle);

    float mXDir = -(float) Math.cos(radianAngle);
    float mYDir = -(float) Math.sin(radianAngle);

    float speedFactor = weaponProfile.forceFactor;
    Vector2 impulse = new Vector2(speedFactor * mXDir / Settings.PPM, speedFactor * mYDir / Settings.PPM);
    bulletBody.applyLinearImpulse(impulse, bulletBody.getPosition(), true);

    return true;
  }

  @Override
  public void update() {
    updateSpritePositionForBody(false);
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
