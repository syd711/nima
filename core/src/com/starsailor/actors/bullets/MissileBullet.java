package com.starsailor.actors.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.Ship;
import com.starsailor.components.ComponentFactory;
import com.starsailor.model.WeaponData;
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

  public MissileBullet(WeaponData weaponData, Ship owner, Ship target) {
    super(weaponData, owner, target);
  }

  @Override
  protected void createComponents(WeaponData weaponData) {
    super.createComponents(weaponData);

    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, weaponData.getSteeringData());
    SteeringManager.setMissileSteering(steerableComponent, target.steerableComponent);
    steerableComponent.setIndependetFacing(false);
    steerableComponent.setEnabled(false);
  }

  @Override
  public boolean create() {
    //apply initial force to the missile
    Body bulletBody = bodyComponent.body;
    Body ownerBody = owner.shipBodyComponent.body;
    if(ownerBody == null) {
      return false;
    }

    //align bullet with bullet owner
    bulletBody.setTransform(bulletBody.getPosition(), ownerBody.getAngle());

    float angle = ownerBody.getAngle();
    angle = Box2dUtil.addDegree(angle, 90);
    Vector2 force = new Vector2();
    force.x = (float) Math.cos(angle);
    force.y = (float) Math.sin(angle);
    force = force.scl(weaponData.getForceFactor() * Game.camera.zoom);

    bulletBody.applyForceToCenter(force, true);
    spineComponent.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()) - 90);

    //add dependency tracking for the target
    EntityManager.getInstance().addEntityListener(this);

    return true;
  }

  @Override
  public void update() {
    updatePosition();

    if(!steerableComponent.isEnabled()) {
      float distanceToOwner = getDistanceFromOrigin() * Game.camera.zoom;
      //lazy init of the bullet's steering system
      if(distanceToOwner > weaponData.getActivationDistance() && !steerableComponent.isEnabled()) {
        steerableComponent.setEnabled(true);
      }

      if(this.target.isMarkedForDestroy()) {
        destroyWithoutHit();
        return;
      }
    }
    else {
      //maybe the target has already been destroyed
      if(target == null || target.isMarkedForDestroy()) {
        //select next target then
        target = this.owner.findNearestEnemy(true);
      }

      //TODO not so sure here
      if(target == null) {
        destroyWithoutHit();
        return;
      }

      //by default the target body is what we aim for
      Body targetBody = target.shipBodyComponent.body;

      //check for flares
      Bullet nearestEnemyFlare = findNearestForeignFlare(true);
      if(nearestEnemyFlare != null) {
        //attack flare if the distance is shorter
        if(target.getDistanceTo(this) > this.getDistanceTo(nearestEnemyFlare)) {
          targetBody = nearestEnemyFlare.bodyComponent.body;
          steerableComponent.setEnabled(false);
        }
      }
      //may it has been destroyed during flying
      if(targetBody != null) {
        Box2dUtil.gravity(bodyComponent.body, targetBody, 1.1f);
        spineComponent.setRotation((float) Math.toDegrees(bodyComponent.body.getAngle()) - 90);
      }
      else {
        steerableComponent.setEnabled(true);
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
    if(bullet instanceof FlaresBullet) {
      hitAndDestroyBullet(position, Resources.SOUND_EXPLOSION);
    }
  }

  @Override
  public List<WeaponData.Types> getDefensiveWeapons() {
    return Arrays.asList(WeaponData.Types.FLARES);
  }

  // --------------------------  Helper ---------------------

  private void destroyWithoutHit() {
    hitAndDestroyBullet(getPosition(), Resources.SOUND_EXPLOSION);
  }

  /**
   * Returns the nearest flare for this missile.
   */
  private Bullet findNearestForeignFlare(boolean onlyEnemyFlares) {
    List<Bullet> result = new ArrayList<>();
    //now check if there are flares to update the target
    List<FlaresBullet> flares = EntityManager.getInstance().getEntities(FlaresBullet.class);
    for(FlaresBullet flare : flares) {
      if(!flare.owner.equals(this.owner) && flare.bodyComponent.body.isActive()) { //body is still active
        if(onlyEnemyFlares && owner.getFormationMembers().contains(flare.owner)) {
          continue;
        }
        result.add(flare);
      }
    }

    Bullet nearest = null;
    for(Bullet flare : result) {
      if(nearest == null) {
        nearest = flare;
        continue;
      }

      float distance = flare.bodyComponent.distanceTo(bodyComponent.body);
      if(distance < nearest.bodyComponent.distanceTo(bodyComponent.body)) {
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
