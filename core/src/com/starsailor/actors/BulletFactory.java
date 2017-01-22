package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SoundManager;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.Resources;
import com.starsailor.util.Settings;

/**
 *
 */
public class BulletFactory {

  public static void fireBullet(Ship owner, Ship target) {
    WeaponProfile weaponProfile = owner.shootingComponent.getActiveWeaponProfile();
    Bullet bullet = new Bullet(weaponProfile, owner, target);

    Vector2 from = Box2dUtil.toBox2Vector(owner.getCenter());
    Vector2 to = Box2dUtil.toBox2Vector(target.getCenter());
    float radianAngle = Box2dUtil.getBox2dAngle(from, to);

    Body bulletBody = bullet.bodyComponent.body;
    bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, radianAngle);

    float mXDir = -(float) Math.cos(radianAngle);
    float mYDir = -(float) Math.sin(radianAngle);

    float speedFactor = weaponProfile.speed;
    Vector2 impulse = new Vector2(speedFactor * mXDir / Settings.PPM, speedFactor * mYDir / Settings.PPM);
    bulletBody.applyLinearImpulse(impulse, bulletBody.getPosition(), true);

    owner.shootingComponent.updateLastBulletTime();

    EntityManager.getInstance().add(bullet);

    SoundManager.playSoundAtPosition(Resources.SOUND_LASER, 0.5f, new Vector3(owner.getCenter().x, owner.getCenter().y, 0));
  }
}
