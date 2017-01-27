package com.starsailor.actors.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.Ship;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.collision.BulletCollisionComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.systems.behaviours.FaceBehaviourImpl;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a weapon type.
 */
public class MissileBullet extends Bullet implements EntityListener {

  private Pursue<Vector2> behaviour;
  private Bullet nearestFlare;

  public MissileBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void createComponents(WeaponProfile weaponProfile) {
    super.createComponents(weaponProfile);

    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponProfile.steeringData);
    behaviour = new Pursue<>(steerableComponent, target.steerableComponent);
    behaviour.setMaxPredictionTime(0f);
    steerableComponent.setBehavior(behaviour);
    steerableComponent.setFaceBehaviour(new FaceBehaviourImpl(bodyComponent.body, target.bodyComponent.body, 3f));
    steerableComponent.setEnabled(false);
  }

  @Override
  protected void create() {
    //add dependency tracking for the target
    EntityManager.getInstance().addEntityListener(this);

    //apply initial force to the missile
    Body bulletBody = bodyComponent.body;
    Body ownerBody = owner.bodyComponent.body;
    bulletBody.setTransform(bulletBody.getPosition(), ownerBody.getAngle());

    float angle = ownerBody.getAngle();
    angle = Box2dUtil.addDegree(angle, 90);
    Vector2 force = new Vector2();
    force.x = (float) Math.cos(angle);
    force.y = (float) Math.sin(angle);
    force = force.scl(weaponProfile.forceFactor);

    bulletBody.applyForceToCenter(force, true);
  }

  @Override
  public void update() {
    updateSpritePositionForBody(false);

    if(!steerableComponent.isDestroyed()) {
      float distanceToPlayer = getDistanceFromOrigin();
      //lazy init of the bullet's steering system
      if(distanceToPlayer > weaponProfile.activationDistance && !steerableComponent.isEnabled()) {
        steerableComponent.setEnabled(true);

        //check for flares
        nearestFlare = findNearestEnemyFlare();
        if(nearestFlare != null) {
          behaviour.setTarget(nearestFlare.steerableComponent);
          steerableComponent.getFaceBehaviour().setTarget(nearestFlare.bodyComponent.body);
        }
      }
      else {
        getSpriteItem().setRotation((float) Math.toDegrees(bodyComponent.body.getAngle())-90);
      }
    }
  }

  @Override
  public void collide(Ship ship, Vector2 position) {
    hitAndDestroyBullet(position, Resources.SOUND_EXPLOSION);
    updateDamage(ship);
  }

  @Override
  public void collide(Bullet bullet, Vector2 position) {
    hitAndDestroyBullet(position, Resources.SOUND_EXPLOSION);
    EntityManager.getInstance().destroy(bullet);
  }

  // --------------------------  Helper ---------------------

  private Bullet findNearestEnemyFlare() {
    List<Bullet> flares = new ArrayList<>();
    //now check if there are flares to update the target
    ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(BulletCollisionComponent.class);
    for(Entity e : entitiesFor) {
      Bullet b = (Bullet) e;
      //filter
      if(!b.owner.equals(this.owner)
          && b.weaponProfile.type.equals(WeaponProfile.Types.FLARES)
          && b.bodyComponent.body.isActive()) {
        flares.add(b);
      }
    }

    Bullet nearest = null;
    float nearestDistance = 10000;
    for(Bullet flare : flares) {
      float distance = flare.bodyComponent.distanceTo(bodyComponent.body);
      if(distance < nearestDistance) {
        nearest = flare;
      }
    }
    return nearest;
  }


  //------------------ Auto Destroy ---------------------------------------------

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    //check if a flare that is currently targeted is burned out
    if(entity.equals(nearestFlare)) {
      behaviour.setTarget(target.steerableComponent);
      steerableComponent.getFaceBehaviour().setTarget(target.bodyComponent.body);
    }

    //check if the target is already destroyed by a previous bullet
    if(entity.equals(target)) {
      if(steerableComponent != null) {
        steerableComponent.setDestroyed(true);
      }
    }
  }
}
