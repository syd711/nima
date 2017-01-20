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

  public static void fireBullet(Ship owner, Ship target) {
    WeaponProfile weaponProfile = owner.shootingComponent.getActiveWeaponProfile();
    Bullet bullet = new Bullet(weaponProfile, owner, target);

    Vector2 from = Box2dUtil.toBox2Vector(owner.getCenter());
    Vector2 to = Box2dUtil.toBox2Vector(target.getCenter());
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

    SoundManager.playSoundAtPosition("sounds/laser.wav", 0.5f, new Vector3(owner.getCenter().x, owner.getCenter().y, 0));
  }

  private Bullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    this.weaponProfile = weaponProfile;
    this.owner = owner;
    this.target = target;

    spriteComponent = ComponentFactory.addSpriteComponent(this, weaponProfile.name);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(owner.getCenter());
    positionComponent.z = 900;
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, 10);
    bodyComponent = ComponentFactory.addBulletBodyComponent(this, owner.getCenter(), owner instanceof Player);

    ComponentFactory.addBulletCollisionComponent(this);
    Gdx.app.log(getClass().getName(), owner + " is firing " + this + " at " + target);
  }

  public boolean is(String weaponType) {
    return weaponProfile.name.equalsIgnoreCase(weaponType);
  }

  public boolean isOwner(Entity entity) {
    return owner.equals(entity);
  }

  public void applyCollisionWith(Ship npc) {
    BodyComponent component = npc.getComponent(BodyComponent.class);

    //use my linear velocity
    Vector2 linearVelocity = bodyComponent.body.getLinearVelocity();
    float impactFactor = weaponProfile.impactFactor;
    Vector2 force = new Vector2(linearVelocity.x*impactFactor, linearVelocity.y*impactFactor);
    //to apply it on the target
    component.body.applyForceToCenter(force.x, force.y, true);
    if(!isOwner(npc)) {
      EntityManager.getInstance().destroy(this);
    }
  }
}
