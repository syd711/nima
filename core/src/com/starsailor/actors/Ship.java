package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.bullets.BulletFactory;
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
abstract public class Ship extends Spine implements FormationMember<Vector2>, Telegraph, EntityListener {
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

  public ShipProfile shipProfile;
  public float health = 100;
  public float maxHealth = 100;

  private Box2dLocation location;

  //battle variables
  public Ship attacking;
  public Ship attacker;

  //set if this entity is part of a formation
  public Ship formationOwner;

  //only used during initializing
  private Vector2 position;

  public Ship(ShipProfile profile, Vector2 position) {
    super(Resources.SPINES + profile.spine + "/" + profile.spine, profile.defaultAnimation, profile.scale);
    this.shipProfile = profile;
    this.position = position;

    EntityManager.getInstance().addEntityListener(this);
  }

  public void createComponents(ShipProfile profile) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this, profile.bodyData, position);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, profile.steeringData);
    shootingComponent = ComponentFactory.addShootableComponent(this, profile);
    particleComponent = ComponentFactory.addParticleComponent(this, Particles.EXPLOSION);
    shieldComponent = ComponentFactory.addShieldComponent(this, profile.shieldProfile);
    formationComponent = ComponentFactory.addFormationComponent(this, steerableComponent, 150f);

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
   * Formation members can only be NPCs
   *
   * @param member
   */
  public void addFormationMember(NPC member) {
    formationComponent.addMember(member);
  }

  public List<NPC> getFormationMembers() {
    return formationOwner.formationComponent.members;
  }

  /**
   * Applies the damage to the shield or the ship health.
   *
   * @param bullet the damage to apply for the shield or health.
   * @return True if the damage destroyed this entity.
   */
  public void applyDamageFor(Bullet bullet) {
    Ship attackerFromBullet = getAttackerFromBullet(bullet);
    if(attacker == null) {
      //apply data to formation owner
      formationOwner.attacker = attackerFromBullet;
      applyFormationState(new AttackedState());
    }

    BulletDamageComponent damageComponent = bullet.getComponent(BulletDamageComponent.class);
    float damage = damageComponent.damage;
    float damageOffset = damage; //the additional value to substract from health
    if(shieldComponent.isActive()) {
      damageOffset = shieldComponent.applyDamage(damage);
    }
    health = health - damageOffset;

    if(health <= 0) {
      //trigger particle effect
      if(particleComponent != null) {
        particleComponent.enabled = true;
      }
      destroy();
    }

    if(!shieldComponent.isActive() && shieldComponent.isRemaining()) {
      setShieldEnabled(true);
    }

    if(!shieldComponent.isActive() || !shieldComponent.isRemaining()) {
      setShieldEnabled(false);
    }
  }

  /**
   * Handling the entity removal from the Ashley engine, etc.
   */
  protected void destroy() {
    EntityManager.getInstance().destroy(this);
  }

  /**
   * Fires a bullet using the active weapon profile
   */
  public void fireAtTarget() {
    BulletFactory.create(this, attacking);
  }

  /**
   * The given target is used to shoot at
   *
   * @param npc
   */
  public void lockTarget(Ship npc) {
    attacking = npc;
  }

  public StateMachine getStateMachine() {
    return statefulComponent.stateMachine;
  }

  /**
   * Used the button in HUD is pressed
   */
  public void switchWeapon(WeaponProfile weaponProfile) {
    shootingComponent.setActiveWeaponProfile(weaponProfile);
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
   * Searches for an enemy to shoot at.
   * The entities "attackDistance" is used for this which means
   * that the ship itself has not necessarily a weapon in shooting range.
   *
   * @return True if an enemy was found to shoot at
   */
  public boolean findAndLockNearestTarget() {
    //TODO filter for friends!
    float attackDistance = shipProfile.attackDistance;
    Ship nearestNeighbour = findNearestNeighbour();
    if(nearestNeighbour != null) {
      float distanceToEnemy = nearestNeighbour.getDistanceTo(this);
      if(distanceToEnemy != 0 && distanceToEnemy < attackDistance) {
        setShieldEnabled(true);
        lockTarget(nearestNeighbour);
        return true;
      }
    }
    return false;
  }

  public Ship findNearestNeighbour() {
    Ship nearestNeighbour = null;
    //TODO not necessarily a spine
    ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(SpineComponent.class);
    for(Entity entity : entitiesFor) {
      Ship ship = (Ship) entity;
      if(ship.equals(this)) {
        continue;
      }
      if(nearestNeighbour == null) {
        nearestNeighbour = ship;
        continue;
      }

      if(this.getDistanceTo(ship) < this.getDistanceTo(nearestNeighbour)) {
        nearestNeighbour = ship;
      }
    }
    return nearestNeighbour;
  }

  //-------------- Messaging Service ---------------------------------------------------------------------

  @Override
  public boolean handleMessage(Telegram msg) {
    switch(msg.message) {
//      case ATTACKED: {//usually triggerd by bullet collision message
//        return true;
//      }
//      case ATTACK: {
//        return true;
//      }
    }

    return false;
  }

  //-------------- Entity Listener -----------------------------------------------------------------------

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    if(attacker != null && entity.equals(attacker)) {
      this.attacker = null;
    }
    if(attacking != null && entity.equals(attacking)) {
      this.attacking = null;
    }
  }


  //------------- Helper ----------------------------------------------------------------------------------

  private void applyFormationState(State<NPC> state) {
    //notify my members that I am under attack
    List<NPC> formationMembers = getFormationMembers();
    for(NPC formationMember : formationMembers) {
      if(!formationMember.equals(this)) {
        formationMember.getStateMachine().changeState(state);
      }
    }
    formationOwner.getStateMachine().changeState(state);
  }

  /**
   * Check for friendly fire
   *
   * @param bullet the bullet the ship has been hit with
   * @return null if the attacker is a friend
   */
  private Ship getAttackerFromBullet(Bullet bullet) {
    Ship owner = bullet.owner;
    List<NPC> formationMembers = getFormationMembers();
    for(NPC formationMember : formationMembers) {
      if(owner.equals(formationMember)) {
        return null;
      }
    }

    return owner;
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
}
