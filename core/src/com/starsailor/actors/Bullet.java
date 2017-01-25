package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.components.*;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.Particles;
import com.starsailor.managers.Textures;

/**
 * Entity for bullets
 */
public class Bullet extends GameEntity implements EntityListener {
  public SpriteComponent spriteComponent;
  public PositionComponent positionComponent;
  public SteerableComponent steerableComponent;
  public BulletDamageComponent bulletDamageComponent;
  public ParticleComponent particleComponent;
  public BodyComponent bodyComponent;

  public WeaponProfile weaponProfile;
  public Ship owner;
  public Ship target;

  private Vector2 origin;

  private long shootingTime = 0;

  public Bullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    shootingTime = System.currentTimeMillis();

    this.weaponProfile = weaponProfile;
    this.origin = owner.getCenter();
    this.owner = owner;
    this.target = target;

    spriteComponent = ComponentFactory.addSpriteComponent(this, Textures.valueOf(weaponProfile.type.name().toUpperCase()), 90);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(owner.getCenter());
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, weaponProfile);
    particleComponent = ComponentFactory.addParticleComponent(this, Particles.valueOf(weaponProfile.name.toUpperCase() + "_BULLET_HIT"));

    //not all bullets require a body
    if(weaponProfile.bodyData != null) {
      bodyComponent = ComponentFactory.addBulletBodyComponent(this, owner.getCenter(), weaponProfile, owner instanceof Player);
    }

    ComponentFactory.addBulletCollisionComponent(this);
    Gdx.app.log(getClass().getName(), owner + " is firing " + this + " at " + target);
  }

  public boolean is(WeaponProfile.Types type) {
    return weaponProfile.type.equals(type);
  }

  public boolean isOwner(Entity entity) {
    return owner.equals(entity);
  }

  public float getDistanceFromOrigin() {
    return positionComponent.getPosition().dst(origin);
  }


  public boolean isExhausted() {
    float current = Game.currentTimeMillis - shootingTime;
    return current > weaponProfile.durationMillis;
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    //check if the target is already destroyed
    if(entity.equals(target)) {
      if(steerableComponent != null) {
        steerableComponent.setDestroyed(true);
      }
    }
  }
}
