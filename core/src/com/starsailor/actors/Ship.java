package com.starsailor.actors;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.npc.AttackState;
import com.starsailor.actors.states.npc.AttackedState;
import com.starsailor.components.*;
import com.starsailor.data.ShieldProfile;
import com.starsailor.data.ShipProfile;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.Particles;
import com.starsailor.managers.Textures;
import com.starsailor.util.Resources;
import com.starsailor.util.box2d.Box2dLocation;

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
  public SpriteComponent spriteComponent;
  public FormationComponent formationComponent;
  public FractionComponent fractionComponent;
  public HealthComponent healthComponent;

  public ShipProfile shipProfile;

  private Box2dLocation location;

  //only used during initializing
  private Vector2 position;

  public Ship(ShipProfile profile, Vector2 position) {
    super(Resources.SPINES + profile.spine + "/" + profile.spine, profile.defaultAnimation, profile.scale);
    this.shipProfile = profile;
    this.position = position;
  }

  public void createComponents(Fraction fraction) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this, shipProfile.bodyData, position);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, shipProfile.steeringData);
    shootingComponent = ComponentFactory.addShootableComponent(this, shipProfile);
    particleComponent = ComponentFactory.addParticleComponent(this, Particles.EXPLOSION);
    shieldComponent = ComponentFactory.addShieldComponent(this, shipProfile.shieldProfile);
    healthComponent = ComponentFactory.addHealthComponent(this, shipProfile.shieldProfile);
    fractionComponent = ComponentFactory.createFractionComponent(this, fraction);
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, shipProfile.formationDistance);

    spriteComponent = ComponentFactory.addSpriteComponent(this, Textures.SELECTION, -1);
    spriteComponent.addSprite(Textures.HEALTHBG);
    spriteComponent.addSprite(Textures.HEALTHFG);

    this.location = new Box2dLocation(new Vector2());
  }

  public ShieldProfile getShield() {
    return shipProfile.shieldProfile;
  }

  public List<WeaponProfile> getWeapons() {
    return shipProfile.weaponProfiles;
  }

  /**
   * Applies the damage to the shield or the ship health.
   *
   * @param bullet the damage to apply for the shield or health.
   * @return True if the damage destroyed this entity.
   */
  public void applyDamageFor(Bullet bullet) {
    if(!isAlreadyInBattle()) {
      moveToAttackedState(bullet);
    }
    updateAttackState(bullet);
    updateDamage(bullet);
  }

  public boolean isEnemyOf(Ship ship) {
    return !fractionComponent.fraction.equals(ship.fractionComponent.fraction);
  }


  /**
   * Handling the entity removal from the Ashley engine, etc.
   */
  protected void destroy() {
    EntityManager.getInstance().destroy(this);
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

  //------------ To be implemented ------------------------------------------------------------------------

  abstract public State getDefaultState();


  //------------- Helper ----------------------------------------------------------------------------------

  /**
   * Return true if the entity is already in a battle state
   * @return
   */
  private boolean isAlreadyInBattle() {
    State currentState = getStateMachine().getCurrentState();
    return currentState instanceof AttackedState || currentState instanceof AttackState;
  }

  /**
   * Switches this entity to the attacked state if not already there.
   *
   * @param bullet the attacker bullet
   */
  public void moveToAttackedState(Bullet bullet) {
    State currentState = getStateMachine().getCurrentState();
    if(!(currentState instanceof AttackedState)) {
      getStateMachine().changeState(new AttackedState(bullet));
    }
  }

  /**
   * Updates the last bullet for the attack state
   *
   * @param bullet the attacker bullet
   */
  private void updateAttackState(Bullet bullet) {
    State currentState = getStateMachine().getCurrentState();
    if(currentState instanceof AttackState) {
      ((AttackState) getStateMachine().getCurrentState()).hitBy(bullet);
    }
  }

  /**
   * Updates shield and health damage for the given bullet
   *
   * @param bullet the attacker's bullet
   */
  protected void updateDamage(Bullet bullet) {
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
    }
    else {
      if(!shieldComponent.isRemaining()) {
        setShieldEnabled(false);
      }
    }
  }


  /**
   * Enable the shild component and the visual elements for it
   *
   * @param enabled
   */
  public void setShieldEnabled(boolean enabled) {
    if(enabled) {
      if(!shieldComponent.isActive()) {
        spriteComponent.addSprite(Textures.SHIELDBG);
        spriteComponent.addSprite(Textures.SHIELDFG);
        shieldComponent.setActive(true);
        spriteComponent.setEnabled(true);
      }
    }
    else {
      spriteComponent.removeSprite(Textures.SHIELDBG);
      spriteComponent.removeSprite(Textures.SHIELDFG);
    }
  }

  public boolean isShieldEnabled() {
    return shieldComponent.isActive();
  }
}
