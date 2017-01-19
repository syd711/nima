package com.nima.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.*;
import com.nima.data.ShipProfile;
import com.nima.util.Resources;

/**
 * The general ship entity which is always a spine.
 */
public class Ship extends Spine {
  public StatefulComponent statefulComponent;
  public SteerableComponent steerableComponent;
  public SpineComponent spineComponent;
  public SpeedComponent speedComponent;
  public ScalingComponent scalingComponent;
  public RotationComponent rotationComponent;
  public ShootingComponent shootingComponent;
  public PositionComponent positionComponent;
  public DamageComponent damageComponent;
  public BodyComponent bodyComponent;
  public SelectionComponent selectionComponent;
  public SpriteComponent spriteComponent;

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
    speedComponent = ComponentFactory.addSpeedComponent(this, profile);
    rotationComponent = ComponentFactory.addRotationComponent(this, profile);
    bodyComponent = ComponentFactory.addBodyComponent(this);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, profile);
    shootingComponent = ComponentFactory.addShootableComponent(this, profile);
    damageComponent = ComponentFactory.addDamageComponent(this, profile);
    selectionComponent = ComponentFactory.addSelectionComponent(this);
  }

  /**
   * Fires a bullet using the active weapon profile
   * @param worldCoordinates the target to shoot to
   */
  public void fireAt(Vector2 worldCoordinates) {
    if(shootingComponent.isCharged()) {
      Bullet.fireBullet(this, getCenter(), worldCoordinates);
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
   * Weapon number starting from 1
   */
  public void switchWeapon(int weaponNumber) {
    shootingComponent.setActiveWeaponProfile(weaponNumber);
  }

  public void toggleSelection() {
    boolean selected = selectionComponent.toggleSelection();
    if(selected) {
      spriteComponent = ComponentFactory.addSpriteComponent(this, "selection");
    }
    else {
      this.remove(SpriteComponent.class);
    }
  }
}
