package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.npc.BattleState;
import com.starsailor.components.*;
import com.starsailor.managers.EntityManager;
import com.starsailor.model.ShieldData;
import com.starsailor.model.ShipData;
import com.starsailor.model.WeaponData;
import com.starsailor.model.items.ShipItem;
import com.starsailor.savegame.SaveGameItem;
import com.starsailor.savegame.Saveable;
import com.starsailor.util.box2d.Box2dLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.starsailor.util.Settings.PPM;

/**
 * The general ship entity which is always a spine.
 */
abstract public class Ship extends GameEntity implements IFormationMember<Ship>, Saveable {
  public StatefulComponent statefulComponent;
  public SteerableComponent steerableComponent;
  public ScalingComponent scalingComponent;
  public ShootingComponent shootingComponent;
  public PositionComponent positionComponent;
  public BodyShipComponent bodyShipComponent;
  public ParticleComponent particleComponent;

  public ShieldStatusComponent shieldStatusComponent;
  public ShieldSpineComponent shieldSpineComponent;

  public HealthComponent healthComponent;
  public FractionComponent fractionComponent;
  public SpineShipComponent spineShipComponent;

  public ShipData shipData;
  protected ShipItem shipItem;

  private IFormationOwner formationOwner;
  private Box2dLocation location;

  //only used during initializing
  private Vector2 position;

  public Ship(ShipItem shipItem, Vector2 position) {
    this.shipItem = shipItem;
    this.shipData = shipItem.getShipData();
    this.position = position;
  }

  public void createComponents() {
    ComponentFactory.addSpineMarkerComponent(this);
    spineShipComponent = ComponentFactory.addSpineShipComponent(this, shipItem.getShipData().getSpineData());

    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this);

    bodyShipComponent = ComponentFactory.addShipBodyComponent(this, shipData.getBodyData(), position);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyShipComponent.body, shipData.getSteeringData());
    shootingComponent = ComponentFactory.addShootableComponent(this, shipData);
    particleComponent = ComponentFactory.addParticleComponent(this, "explosion"); //TODO json

    shieldStatusComponent = ComponentFactory.addShieldComponent(this, shipData.getStatusData().getShieldData());
    shieldSpineComponent = ComponentFactory.addSpineShieldComponent(this, shipData.getStatusData().getShieldData().getSpineData());
    healthComponent = ComponentFactory.addHealthComponent(this, shipData);

    fractionComponent = ComponentFactory.createFractionComponent(this, Fraction.valueOf(shipItem.getFraction().toUpperCase()));

    this.location = new Box2dLocation(new Vector2());
  }

  @Override
  public void save(SaveGameItem item) {
    item.store("id", shipItem.getId());
    item.store("type", shipItem.getShipType());
    item.store("positionX", positionComponent.x);
    item.store("positionY", positionComponent.y);
    item.store("state", statefulComponent.stateMachine.getCurrentState());
    item.store("shieldHealth", shieldStatusComponent.health);
    item.store("shipHealth", healthComponent.health);
  }

  public void changeState(State state) {
    statefulComponent.stateMachine.changeState(state);
  }

  public float getHeight() {
    return spineShipComponent.getHeight();
  }

  public float getWidth() {
    return spineShipComponent.getWidth();
  }

  public Vector2 getCenter() {
    return new Vector2(bodyShipComponent.body.getPosition()).scl(PPM);
  }

  @Override
  public void setFormationOwner(IFormationOwner formationOwner) {
    this.formationOwner = formationOwner;
  }

  public int getItemId() {
    return shipItem.getId();
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
   * Returns the distance to another ship
   */
  public float getDistanceTo(IFormationOwner formationOwner) {
    BodyComponent component = ((GameEntity) formationOwner).getComponent(BodyComponent.class);
    return component.getWorldPosition().dst(this.getCenter());
  }

  /**
   * Returns the body distance to bullet
   */
  public float getDistanceTo(Bullet bullet) {
    Vector2 position1 = bodyShipComponent.body.getPosition();
    Vector2 position2 = bullet.bodyComponent.body.getPosition();
    return position1.dst(position2);
  }

  /**
   * Enable the shild component and the visual elements for it
   */
  public void setStateVisible(boolean enabled) {
    setShieldEnabled(true);
    healthComponent.setActive(enabled);
  }

  /**
   * Returns to the state that has been passed as default state in the constructor.
   */
  public void switchToDefaultState() {
    changeState(getDefaultState());
    setShieldEnabled(false);
  }

  /**
   * Switches this entity to the attacked state if not already there.
   *
   * @param enemy the enemy ship that has been fired or detected
   */
  public void switchToBattleState(Ship enemy) {
    if(isInDefaultState()) {
      //update the battle state since it is only updated, not recreated
      getBattleState().updateEnemyList(enemy);
      //then switch to the state...
      changeState(getBattleState());

      //...and notify all members that 'we' are attacked
      List<Ship> groupMembers = getFormationMembers();
      for(Ship formationMember : groupMembers) {
        if(!formationMember.equals(this)) {
          formationMember.switchToBattleState(enemy);
        }
      }
    }
    else if(isInBattleState()) {
      getBattleState().updateEnemyList(enemy);
      List<Ship> groupMembers = getFormationMembers();
      for(Ship formationMember : groupMembers) {
        if(!formationMember.equals(this)) {
          formationMember.getBattleState().updateEnemyList(enemy);
        }
      }
    }
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
    if(shieldStatusComponent.isActive()) {
      damageOffset = shieldStatusComponent.applyDamage(damage);
      if(damageOffset > 0) {
        setShieldEnabled(false);
      }
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

  private void setShieldEnabled(boolean enabled) {
    shieldStatusComponent.setActive(enabled);
    shieldSpineComponent.setEnabled(enabled);
    if(enabled) {
      bodyShipComponent.setTargetRadius(shipData.getBodyData().getRadius() * shieldSpineComponent.getJsonScaling());
    }
    else {
      bodyShipComponent.setTargetRadius(shipData.getBodyData().getRadius() / 2 * shieldSpineComponent.getJsonScaling());
    }
  }

  //------------ To be implemented ------------------------------------------------------------------------

  abstract protected State getDefaultState();

  abstract protected BattleState getBattleState();

  //------------- Helper ----------------------------------------------------------------------------------

  public ShipItem getShipItem() {
    return shipItem;
  }

  /**
   * Return true if the entity is already in a battle state
   *
   * @return
   */
  public boolean isInDefaultState() {
    State currentState = statefulComponent.stateMachine.getCurrentState();
    return currentState.equals(getDefaultState());
  }

  public boolean isInBattleState() {
    State currentState = statefulComponent.stateMachine.getCurrentState();
    return currentState.equals(getBattleState());
  }

  public List<Ship> getFormationMembers() {
    if(formationOwner != null) {
      return formationOwner.getMembers();
    }
    return Arrays.asList(this);
  }

  public IFormationOwner getFormationOwner() {
    return formationOwner;
  }
}
