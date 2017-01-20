package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.components.*;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SoundManager;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.Settings;

/**
 * Entity for bullets
 */
public class Bullet extends GameEntity {
  public SpriteComponent spriteComponent;
  public PositionComponent positionComponent;
  public BulletDamageComponent bulletDamageComponent;
  public BodyComponent bodyComponent;

  public WeaponProfile weaponProfile;
  public Entity owner;
  public Ship target;

  public static void fireBullet(Ship owner, Vector2 fromWorld, Vector2 toWorld) {
    WeaponProfile weaponProfile = owner.shootingComponent.getActiveWeaponProfile();
    Bullet bullet = new Bullet(weaponProfile, owner, fromWorld, toWorld);

    Vector2 from = Box2dUtil.toBox2Vector(fromWorld);
    Vector2 to = Box2dUtil.toBox2Vector(toWorld);
    float radianAngle = Box2dUtil.getBox2dAngle(from, to);

    Body bulletBody = bullet.bodyComponent.body;
    com.badlogic.gdx.graphics.g2d.Sprite sprite = bullet.spriteComponent.sprite;
    bulletBody.setTransform(bulletBody.getPosition().x, bulletBody.getPosition().y, radianAngle);

    float mXDir = -(float) Math.cos(radianAngle);
    float mYDir = -(float) Math.sin(radianAngle);

    float speedFactor = weaponProfile.speed;
    Vector2 impulse = new Vector2(speedFactor * mXDir / Settings.PPM, speedFactor * mYDir / Settings.PPM);
    bulletBody.applyLinearImpulse(impulse, bulletBody.getPosition(), true);
    sprite.setRotation((float) Math.toDegrees(radianAngle));

    owner.shootingComponent.updateLastBulletTime();

    EntityManager.getInstance().add(bullet);

    SoundManager.playSoundAtPosition("sounds/laser.wav", 0.5f, new Vector3(fromWorld.x, fromWorld.y, 0));
  }

  private Bullet(WeaponProfile weaponProfile, Ship owner, Vector2 fromWorld, Vector2 toWorld) {
    this.weaponProfile = weaponProfile;
    this.owner = owner;
    this.target = (Ship) EntityManager.getInstance().getEntityAt(toWorld.x, toWorld.y);

    spriteComponent = ComponentFactory.addSpriteComponent(this, weaponProfile.name);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(fromWorld);
    positionComponent.z = 900;
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, 10);
    bodyComponent = ComponentFactory.addBulletBodyComponent(this, fromWorld, owner instanceof Player);

    ComponentFactory.addBulletCollisionComponent(this);
    Gdx.app.log(getClass().getName(), owner + " is firing " + this + " at " + target);
  }

  public boolean is(String weaponType) {
    return weaponProfile.name.equalsIgnoreCase(weaponType);
  }

  public boolean isOwner(Entity entity) {
    return owner.equals(entity);
  }
}
