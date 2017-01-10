package com.nima.actors;

import com.nima.components.*;
import com.nima.data.ShipProfile;
import com.nima.util.Resources;

/**
 * The general ship entity which is always a spine.
 */
public class Ship extends Spine {
  protected SteerableComponent steerableComponent;
  protected SpineComponent spineComponent;

  protected SpeedComponent speedComponent;
  protected ScalingComponent scalingComponent;
  protected RotationComponent rotationComponent;

  public Ship(ShipProfile profile) {
    super(Resources.SPINES + profile.spine + "/" + profile.spine, profile.defaultAnimation, profile.scale);
    createComponents(profile);
  }

  protected void createComponents(ShipProfile profile) {
    scalingComponent = ComponentFactory.addScalingComponent(this);
    positionComponent = ComponentFactory.addPositionComponent(this, false, getHeight());
    spineComponent = ComponentFactory.addSpineComponent(this);
    speedComponent = ComponentFactory.addSpeedComponent(this, profile);
    rotationComponent = ComponentFactory.addRotationComponent(this);
    bodyComponent = ComponentFactory.addBodyComponent(this);
    steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, profile);
  }
}
