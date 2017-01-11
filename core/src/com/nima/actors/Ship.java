package com.nima.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;
import com.nima.components.*;
import com.nima.data.DataEntities;
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
  public BodyComponent bodyComponent;

  public Ship(ShipProfile profile) {
    super(Resources.SPINES + profile.spine + "/" + profile.spine, profile.defaultAnimation, profile.scale);
    createComponents(profile);
  }

  protected void createComponents(ShipProfile profile) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    statefulComponent = ComponentFactory.addStatefulComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    speedComponent = ComponentFactory.addSpeedComponent(this, profile);
    rotationComponent = ComponentFactory.addRotationComponent(this, profile);
    bodyComponent = ComponentFactory.addBodyComponent(this);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, profile);
    shootingComponent = ComponentFactory.addShootableComponent(this);
    shootingComponent.weaponProfile = DataEntities.getWeapon(DataEntities.WEAPON_LASER);
  }

  /**
   * Fires a bullet using the active weapon profile
   * @param worldCoordinates the target to shoot to
   */
  public void fireAt(Vector2 worldCoordinates) {
    if(shootingComponent.isCharged()) {
      Bullet.fireBullet(shootingComponent, positionComponent.getPosition(), worldCoordinates);
    }
  }

  public DefaultStateMachine getStateMachine() {
    return statefulComponent.stateMachine;
  }
}
