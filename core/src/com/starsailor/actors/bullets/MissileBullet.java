package com.starsailor.actors.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.collision.BulletCollisionComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.systems.behaviours.FaceBehaviourImpl;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.Resources;

/**
 * Concrete implementation of a weapon type.
 */
public class MissileBullet extends Bullet {

  private Pursue<Vector2> behaviour;

  public MissileBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void create() {
    //add dependency tracking for the target
    EntityManager.getInstance().addEntityListener(this);

    //configure steerable of the missile
    configureSteering();

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

        //now check if there are flares to update the target
        ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(BulletCollisionComponent.class);
        for(Entity e : entitiesFor) {
          Bullet b = (Bullet) e;

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

  // --------------------------  Helper ---------------------

  private void configureSteering() {
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponProfile.steeringData);
    behaviour = new Pursue<>(steerableComponent, target.steerableComponent);
    behaviour.setMaxPredictionTime(0f);
    steerableComponent.setBehavior(behaviour);
    steerableComponent.setFaceBehaviour(new FaceBehaviourImpl(bodyComponent.body, target.bodyComponent.body, 3f));
    steerableComponent.setEnabled(false);
  }
}
