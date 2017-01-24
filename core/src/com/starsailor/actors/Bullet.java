package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.starsailor.components.*;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.*;
import com.starsailor.systems.behaviours.FaceBehaviourImpl;
import com.starsailor.util.Resources;

/**
 * Entity for bullets
 */
public class Bullet extends GameEntity {
  public SpriteComponent spriteComponent;
  public PositionComponent positionComponent;
  public SteerableComponent steerableComponent;
  public BulletDamageComponent bulletDamageComponent;
  public BodyComponent bodyComponent;

  public WeaponProfile weaponProfile;
  public Ship owner;
  public Ship target;

  private Vector2 origin;

  public Bullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    this.weaponProfile = weaponProfile;
    this.origin = owner.getCenter();
    this.owner = owner;
    this.target = target;

    spriteComponent = ComponentFactory.addSpriteComponent(this, Sprites.valueOf(weaponProfile.type.name().toUpperCase()), 90);
    positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(owner.getCenter());
    bodyComponent = ComponentFactory.addBulletBodyComponent(this, owner.getCenter(), weaponProfile, owner instanceof Player);
    bulletDamageComponent = ComponentFactory.addBulletDamageComponent(this, weaponProfile);


    if(weaponProfile.steeringData != null) {
      steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponProfile.steeringData);
      Pursue<Vector2> behaviour = new Pursue<>(steerableComponent, target.steerableComponent);
      behaviour.setMaxPredictionTime(0f);
      steerableComponent.setBehavior(behaviour);
      steerableComponent.setFaceBehaviour(new FaceBehaviourImpl(bodyComponent.body, target.bodyComponent.body, 3f));
      steerableComponent.setEnabled(false);
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

  public void applyCollisionWith(Ship npc, Vector2 position) {
    BodyComponent component = npc.getComponent(BodyComponent.class);

    //use my linear velocity
    Vector2 linearVelocity = bodyComponent.body.getLinearVelocity();
    float impactFactor = weaponProfile.impactFactor;
    Vector2 force = new Vector2(linearVelocity.x*impactFactor, linearVelocity.y*impactFactor);
    //to apply it on the target
    component.body.applyForceToCenter(force.x, force.y, true);

    if(!isOwner(npc)) {
      EntityManager.getInstance().destroy(this);
      SoundManager.playSoundAtPosition(Resources.SOUND_EXPLOSION, 1f, new Vector3(position, 0));
      ParticleManager.getInstance().queueEffect(Particles.EXPLOSION, position, 0.5f);

      ShipDataComponent shipData = npc.shipDataComponent;
      shipData.health = shipData.health-bulletDamageComponent.damage;
      if(shipData.health <= 0 ) {
        EntityManager.getInstance().destroy(npc);
        ParticleManager.getInstance().queueEffect(Particles.EXPLOSION, position);
        ParticleManager.getInstance().queueEffect(Particles.EXPLOSION, position.scl(10));
        ParticleManager.getInstance().queueEffect(Particles.EXPLOSION, position.scl(-10));
      }
    }
  }
}
