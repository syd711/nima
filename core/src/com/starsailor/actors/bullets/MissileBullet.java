package com.starsailor.actors.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.Ship;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.collision.BulletCollisionComponent;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SteeringManager;
import com.starsailor.util.Resources;
import com.starsailor.util.box2d.Box2dUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Concrete implementation of a weapon type.
 */
public class MissileBullet extends Bullet implements EntityListener {

  public MissileBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

  @Override
  protected void createComponents(WeaponProfile weaponProfile) {
    super.createComponents(weaponProfile);

    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponProfile.steeringData);
    SteeringManager.setMissileSteering(steerableComponent, target.steerableComponent);
    steerableComponent.setIndependetFacing(false);
    steerableComponent.setEnabled(false);
  }

  @Override
  public void create() {
    //add dependency tracking for the target
    EntityManager.getInstance().addEntityListener(this);

    //apply initial force to the missile
    Body bulletBody = bodyComponent.body;
    Body ownerBody = owner.bodyComponent.body;

    //align bullet with bullet owner
    bulletBody.setTransform(bulletBody.getPosition(), ownerBody.getAngle());

    float angle = ownerBody.getAngle();
    angle = Box2dUtil.addDegree(angle, 90);
    Vector2 force = new Vector2();
    force.x = (float) Math.cos(angle);
    force.y = (float) Math.sin(angle);
    force = force.scl(weaponProfile.forceFactor * Game.camera.zoom);

    bulletBody.applyForceToCenter(force, true);
    getSpriteItem().setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()) - 90);
  }

  @Override
  public void update() {
    updateSpritePositionForBody(false);

    if(!steerableComponent.isEnabled()) {
      float distanceToOwner = getDistanceFromOrigin() * Game.camera.zoom;
      //lazy init of the bullet's steering system
      if(distanceToOwner > weaponProfile.activationDistance && !steerableComponent.isEnabled()) {
        steerableComponent.setEnabled(true);
      }
    }
    else {
      //by default the target body is what we aim for
      Body targetBody = target.bodyComponent.body;

      //check for flares
      Bullet nearestEnemyFlare = findNearestEnemyFlare();
      if(nearestEnemyFlare != null) {
        targetBody = nearestEnemyFlare.bodyComponent.body;
      }

      //may it has been destroyed during flying
      if(targetBody != null) {
        Box2dUtil.gravity(bodyComponent.body, targetBody, 1.5f);
        getSpriteItem().setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()) - 90);
      }
      else {
        Vector2 linearVelocity = bodyComponent.body.getLinearVelocity();
        bodyComponent.body.setLinearDamping(0);
        bodyComponent.body.setLinearVelocity(linearVelocity);
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
  }

  @Override
  public List<WeaponProfile.Types> getDefensiveWeapons() {
    return Arrays.asList(WeaponProfile.Types.FLARES);
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
    //check if the target is already destroyed by a previous bullet
    if(entity.equals(target)) {
      if(steerableComponent != null) {
        steerableComponent.setEnabled(false);
      }
    }
  }
}
