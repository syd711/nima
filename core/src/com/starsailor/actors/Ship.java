package com.starsailor.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.starsailor.components.*;
import com.starsailor.data.ShieldProfile;
import com.starsailor.data.ShipProfile;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.Particles;
import com.starsailor.util.Resources;

import java.util.List;

/**
 * The general ship entity which is always a spine.
 */
public class Ship extends Spine {
  public StatefulComponent statefulComponent;
  public SteerableComponent steerableComponent;
  public SpineComponent spineComponent;
  public ScalingComponent scalingComponent;
  public ShootingComponent shootingComponent;
  public PositionComponent positionComponent;
  public BodyComponent bodyComponent;
  public ShipDataComponent shipDataComponent;
  public ParticleComponent particleComponent;
  public ShieldComponent shieldComponent;

  public ShipProfile shipProfile;

  public Ship(ShipProfile profile, State state) {
    super(Resources.SPINES + profile.spine + "/" + profile.spine, profile.defaultAnimation, profile.scale);
    this.shipProfile = profile;
    createComponents(profile, state);
  }

  protected void createComponents(ShipProfile profile, State state) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this, state);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, profile.steeringData);
    shootingComponent = ComponentFactory.addShootableComponent(this, profile);
    shipDataComponent = ComponentFactory.addShipDataComponent(this, profile);
    particleComponent = ComponentFactory.addParticleComponent(this, Particles.EXPLOSION);
    shieldComponent = ComponentFactory.addShieldComponent(this, profile.shieldProfile);
  }

  public ShieldProfile getShield() {
    return shipProfile.shieldProfile;
  }

  public List<WeaponProfile> getWeapons() {
    return shipProfile.weaponProfiles;
  }

  /**
   * Fires a bullet using the active weapon profile
   * @param target the target to shoot to
   */
  public void fireAt(Ship target) {
    if(shootingComponent.isCharged()) {
      BulletFactory.fireBullet(this, target);
    }
  }

  /**
   * Ignites the shield is available
   */
  public void fireShield() {
    if(shieldComponent != null && shieldComponent.isCharged()) {

    }
  }

  public float getDistanceToPlayer() {
    return positionComponent.getPosition().dst(Player.getInstance().positionComponent.getPosition());
  }

  public boolean isInShootingRange() {
    return getDistanceToPlayer() < shipProfile.shootDistance;
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
}
