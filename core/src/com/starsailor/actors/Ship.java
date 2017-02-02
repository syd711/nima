package com.starsailor.actors;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.BulletFactory;
import com.starsailor.components.*;
import com.starsailor.data.ShieldProfile;
import com.starsailor.data.ShipProfile;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.Particles;
import com.starsailor.managers.Textures;
import com.starsailor.util.Box2dLocation;
import com.starsailor.util.Resources;

import java.util.List;

/**
 * The general ship entity which is always a spine.
 */
public class Ship extends Spine implements FormationMember<Vector2> {
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

  public ShipProfile shipProfile;
  public float health = 100;
  public float maxHealth = 100;

  private Box2dLocation location;

  public Ship(ShipProfile profile) {
    super(Resources.SPINES + profile.spine + "/" + profile.spine, profile.defaultAnimation, profile.scale);
    this.shipProfile = profile;
  }

  public void createComponents(ShipProfile profile) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this, profile.bodyData);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, profile.steeringData);
    shootingComponent = ComponentFactory.addShootableComponent(this, profile);
    particleComponent = ComponentFactory.addParticleComponent(this, Particles.EXPLOSION);
    shieldComponent = ComponentFactory.addShieldComponent(this, profile.shieldProfile);

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
   * @param damage
   * @return
   */
  public boolean applyDamage(float damage) {
    float damageOffset = damage; //the additional value to substract from health
    if(shieldComponent.isActive()) {
      damageOffset = shieldComponent.applyDamage(damage);
    }
    health = health-damageOffset;

    if(health <= 0) {
      EntityManager.getInstance().destroy(this);
      if(particleComponent != null) {
        particleComponent.enabled = true;
      }
      return true;
    }

    if(!shieldComponent.isActive() && shieldComponent.isRemaining()) {
      spriteComponent.addSprite(Textures.SHIELDBG);
      spriteComponent.addSprite(Textures.SHIELDFG);
      shieldComponent.setActive(true);
    }

    if(!shieldComponent.isActive() || !shieldComponent.isRemaining()) {
      spriteComponent.removeSprite(Textures.SHIELDBG);
      spriteComponent.removeSprite(Textures.SHIELDFG);
    }

    return false;
  }

  /**
   * Fires a bullet using the active weapon profile
   * @param target the target to shoot to
   */
  public void fireAt(Ship target) {
    if(shootingComponent.isCharged()) {
      BulletFactory.create(this, target);
    }
  }

  public float getDistanceToPlayer() {
    return positionComponent.getPosition().dst(Player.getInstance().positionComponent.getPosition());
  }

  public boolean isInShootingRange() {
    return getDistanceToPlayer() != 0 && getDistanceToPlayer() < shipProfile.shootDistance;
  }

  public DefaultStateMachine getStateMachine() {
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
}
