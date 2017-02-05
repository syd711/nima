package com.starsailor.actors.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.limiters.AngularLimiter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.Ship;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.collision.BulletCollisionComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.util.box2d.Box2dUtil;
import com.starsailor.util.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a weapon type.
 */
public class MissileBullet extends Bullet implements EntityListener {

  private BlendedSteering<Vector2> behaviour;
  private Bullet nearestFlare;
  private Pursue pursueSB;

  public MissileBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void createComponents(WeaponProfile weaponProfile) {
    super.createComponents(weaponProfile);

    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponProfile.steeringData);

    //TODO
    LookWhereYouAreGoing lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(steerableComponent) //
        .setLimiter(new AngularLimiter(8, 0.1f)) //
        .setTimeToTarget(0.1f) //
        .setAlignTolerance(0.001f) //
        .setDecelerationRadius(MathUtils.PI);

    pursueSB = new Pursue<>(steerableComponent, target.steerableComponent);
    pursueSB.setMaxPredictionTime(0f);

    behaviour = new BlendedSteering<Vector2>(steerableComponent)
        .setLimiter(NullLimiter.NEUTRAL_LIMITER) //
//        .add(lookWhereYouAreGoingSB, 1f) //
        .add(pursueSB, 1f);


    steerableComponent.setBehavior(behaviour);
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
    force = force.scl(weaponProfile.forceFactor*Game.camera.zoom);

    bulletBody.applyForceToCenter(force, true);
  }

  @Override
  public void update() {
    updateSpritePositionForBody(false);

    if(!steerableComponent.isDestroyed()) {
      float distanceToPlayer = getDistanceFromOrigin() * Game.camera.zoom;
      //lazy init of the bullet's steering system
      if(distanceToPlayer > weaponProfile.activationDistance && !steerableComponent.isEnabled()) {
        steerableComponent.setEnabled(true);

        //check for flares
        nearestFlare = findNearestEnemyFlare();
        if(nearestFlare != null) {
          pursueSB.setTarget(nearestFlare.steerableComponent);
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
      pursueSB.setTarget(target.steerableComponent);
    }

    //check if the target is already destroyed by a previous bullet
    if(entity.equals(target)) {
      if(steerableComponent != null) {
        steerableComponent.setDestroyed(true);
      }
    }
  }
}
