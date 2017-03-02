package com.starsailor.actors;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.components.*;
import com.starsailor.model.ShieldData;
import com.starsailor.model.ShipData;
import com.starsailor.model.WeaponData;
import com.starsailor.managers.EntityManager;
import com.starsailor.util.Resources;
import com.starsailor.util.box2d.Box2dLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The general ship entity which is always a spine.
 */
abstract public class Ship extends Spine implements FormationMember<Vector2> {
  public StatefulComponent statefulComponent;
  public SteerableComponent steerableComponent;
  public SpineComponent spineComponent;
  public ScalingComponent scalingComponent;
  public ShootingComponent shootingComponent;
  public PositionComponent positionComponent;
  public BodyComponent bodyComponent;
  public ParticleComponent particleComponent;
  public ShieldComponent shieldComponent;
  public FormationComponent formationComponent;
  public FractionComponent fractionComponent;
  public HealthComponent healthComponent;

  public ShipData shipData;

  private Box2dLocation location;
  protected String name;

  //only used during initializing
  private Vector2 position;

  public Ship(String name, ShipData profile, Vector2 position) {
    super(Resources.SPINES + profile.getSpineData().getSpine() + "/" + profile.getSpineData().getSpine(), profile.getSpineData());
    this.name = name;
    this.shipData = profile;
    this.position = position;
  }

  public void createComponents(Fraction fraction) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this, shipData.getBodyData(), position);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, shipData.getSteeringData());
    shootingComponent = ComponentFactory.addShootableComponent(this, shipData);
    particleComponent = ComponentFactory.addParticleComponent(this, "explosion"); //TODO json
    shieldComponent = ComponentFactory.addShieldComponent(this, shipData.getStatusData().getShieldData());
    healthComponent = ComponentFactory.addHealthComponent(this, shipData);
    fractionComponent = ComponentFactory.createFractionComponent(this, fraction);
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, shipData.getDistanceData().getFormationDistance());

    this.location = new Box2dLocation(new Vector2());
  }

  public ShieldData getShield() {
    return shipData.getStatusData().getShieldData();
  }

  public List<WeaponData> getChargedWeapons() {
    List<WeaponData> result = new ArrayList<>();
    for(WeaponData weapon : getWeapons()) {
      boolean charged = shootingComponent.isCharged(weapon);
      if(charged) {
        result.add(weapon);
      }
    }
    return result;
  }

  public List<WeaponData> getWeapons() {
    return shipData.getStatusData().getWeaponDatas();
  }

  /**
   * Applies the damage to the shield or the ship health.
   *
   * @param bullet the damage to apply for the shield or health.
   */
  public void applyDamageFor(Bullet bullet) {
    boolean destroyed = updateDamage(bullet);
    //player is also a ship, so we skip here
  }

  public boolean isEnemyOf(Ship ship) {
    return !fractionComponent.fraction.equals(ship.fractionComponent.fraction);
  }

  /**
   * Handling the entity removal from the Ashley engine, etc.
   */
  protected void destroy() {
    markForDestroy();
  }

  public StateMachine getStateMachine() {
    return statefulComponent.stateMachine;
  }

  @Override
  public Location<Vector2> getTargetLocation() {
    return location;
  }

  /**
   * Returns the distance to another ship
   */
  public float getDistanceTo(Ship ship) {
    return ship.getCenter().dst(this.getCenter());
  }

  /**
   * Returns the body distance to bullet
   */
  public float getDistanceTo(Bullet bullet) {
    Vector2 position1 = bodyComponent.body.getPosition();
    Vector2 position2 = bullet.bodyComponent.body.getPosition();
    return position1.dst(position2);
  }

  /**
   * Enable the shild component and the visual elements for it
   */
  public void setStateVisible(boolean enabled) {
    shieldComponent.setActive(enabled);
    healthComponent.setActive(enabled);
  }

  /**
   * Returns to the state that has been passed as default state in the constructor.
   */
  public void switchToDefaultState() {
    getStateMachine().changeState(getDefaultState());
    shieldComponent.setActive(false);
  }

  /**
   * Used for search and destroy.
   * Instead of search for the next enemy of an enemy group, we
   * use all instances of ships to find the nearest target for an attack
   */
  @Nullable
  public Ship findNearestEnemy(boolean withoutPlayer) {
    List<Ship> entities = EntityManager.getInstance().getEntities(Ship.class);
    if(withoutPlayer) {
      entities.remove(Player.getInstance());
    }
    return findNearestEnemyOfGroup(entities);
  }


  /**
   * Returns another ship that is inside the closest attack range and an enemy.
   * The entities "attackDistance" is used for this which means
   * that the ship itself has not necessarily a weapon in shooting range.
   */
  @Nullable
  public Ship findNearestEnemyOfGroup(List<? extends Ship> group) {
    Ship enemy = null;
    for(Ship ship : group) {
      //may all ship entities have passed here, so we filter for ships of another fraction
      if(!ship.isEnemyOf(this)) {
        continue;
      }

      //initial state skip
      float distanceToEnemy = ship.getDistanceTo(this);
      if(distanceToEnemy == 0) {
        continue;
      }

      if(enemy == null) {
        enemy = ship;
        continue;
      }

      //may another ship is closer?
      if(distanceToEnemy < enemy.getDistanceTo(this)) {
        enemy = ship;
      }
    }
    return enemy;
  }


  /**
   * Updates shield and health damage for the given bullet
   *
   * @param bullet the attacker's bullet
   * @return true if this ship has been destroyed
   */
  protected boolean updateDamage(Bullet bullet) {
    BulletDamageComponent damageComponent = bullet.getComponent(BulletDamageComponent.class);
    float damage = damageComponent.damage;
    float damageOffset = damage; //the additional value to substract from health
    if(shieldComponent.isActive()) {
      damageOffset = shieldComponent.applyDamage(damage);
    }
    healthComponent.health = healthComponent.health - damageOffset;

    if(healthComponent.health <= 0) {
      //trigger particle effect
      if(particleComponent != null) {
        particleComponent.enabled = true;
      }
      destroy();
      return true;
    }
    return false;
  }

  //------------ To be implemented ------------------------------------------------------------------------

  abstract protected State getDefaultState();

  //------------- Helper ----------------------------------------------------------------------------------

  /**
   * Return true if the entity is already in a battle state
   *
   * @return
   */
  public boolean isInDefaultState() {
    State currentState = getStateMachine().getCurrentState();
    return currentState.equals(getDefaultState());
  }
}
