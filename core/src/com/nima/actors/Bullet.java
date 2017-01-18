package com.nima.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.components.BulletDamageComponent;
import com.nima.components.ComponentFactory;
import com.nima.data.WeaponProfile;
import com.nima.managers.EntityManager;
import com.nima.managers.SoundManager;
import com.nima.util.Box2dUtil;
import com.nima.util.Settings;

/**
 * Entity for bullets
 */
public class Bullet extends Sprite {
  public final BulletDamageComponent bulletDamageComponent;
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
    super(weaponProfile.name, fromWorld);
    this.weaponProfile = weaponProfile;
    this.owner = owner;
    this.target = (Ship) EntityManager.getInstance().getEntityAt(toWorld.x, toWorld.y);

    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, 10);
    bodyComponent = ComponentFactory.addBodyComponent(this, fromWorld);

    ComponentFactory.addBulletCollisionComponent(this);
    Gdx.app.log(getClass().getName(), owner + " is firing " + this + " at " + target);
  }

  public boolean is(String weaponType) {
    return weaponProfile.name.equalsIgnoreCase(weaponType);
  }

  public float getDistanceToPlayer() {
    return positionComponent.getPosition().dst(Player.getInstance().positionComponent.getPosition());
  }

  public boolean isOwner(Entity entity) {
    return owner.equals(entity);
  }
}
