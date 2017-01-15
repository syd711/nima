package com.nima.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.components.BulletDamageComponent;
import com.nima.components.ComponentFactory;
import com.nima.components.ShootingComponent;
import com.nima.managers.EntityManager;
import com.nima.util.Box2dUtil;
import com.nima.util.Settings;

/**
 * Entity for bullets
 */
public class Bullet extends Sprite {
  private final static String NAME = "laser";

  public final BulletDamageComponent bulletDamageComponent;
  public float damage;

  public static void fireBullet(ShootingComponent shootingComponent, Vector2 fromWorld, Vector2 toWorld) {
    Bullet bullet = new Bullet(NAME, fromWorld);
    bullet.damage = shootingComponent.weaponProfile.damage;

    Vector2 from = Box2dUtil.toBox2Vector(fromWorld);
    Vector2 to = Box2dUtil.toBox2Vector(toWorld);
    float radianAngle = Box2dUtil.getBox2dAngle(from, to);

    Body bulletBody = bullet.bodyComponent.body;
    com.badlogic.gdx.graphics.g2d.Sprite sprite = bullet.spriteComponent.sprite;
    bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, radianAngle);

    float mXDir = -(float) Math.cos(radianAngle);
    float mYDir = -(float) Math.sin(radianAngle);

    float speedFactor = shootingComponent.weaponProfile.speed;
    Vector2 impulse = new Vector2(speedFactor * mXDir / Settings.PPM, speedFactor * mYDir / Settings.PPM);
    bulletBody.applyLinearImpulse(impulse, bulletBody.getPosition(), true);
    sprite.setRotation((float) Math.toDegrees(radianAngle));

    shootingComponent.weaponProfile.lastBulletTime = Game.currentTimeMillis;

    EntityManager.getInstance().add(bullet);
  }

  private Bullet(String name, Vector2 position) {
    super(name, position);
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, 10);
    bodyComponent = ComponentFactory.addBodyComponent(this, name, (short) (Settings.FRIENDLY_BITS | Settings.NEUTRAL_BITS), position, spriteComponent.sprite);
    ComponentFactory.addBulletCollisionComponent(this);
  }


}
