package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.PlayerState;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.components.SteerableComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.data.SteeringData;
import com.starsailor.systems.behaviours.FaceBehaviourImpl;
import com.starsailor.util.Box2dUtil;
import com.starsailor.util.GraphicsUtil;

/**
 * The player with all ashley components.
 */
public class Player extends Ship {
  private static Player instance = null;
  public Entity target;

  private ClickTarget clickTarget;

  public static Player getInstance() {
    return instance;
  }

  public Player(ShipProfile profile) {
    super(profile, PlayerState.IDLE);
    instance = this;
  }

  @Override
  protected void createComponents(ShipProfile profile, State state) {
    super.createComponents(profile, state);
    ComponentFactory.addPlayerCollisionComponent(this);

    //position player
    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
    bodyComponent.setWorldPosition(screenCenter);
  }


  public void moveTo(Vector2 worldCoordinates) {
    if(clickTarget == null) {
      clickTarget = new ClickTarget(worldCoordinates);
      Arrive<Vector2> behaviour = new Arrive<>(steerableComponent, clickTarget.steerableComponent);
      steerableComponent.setBehavior(behaviour);
      steerableComponent.setFaceBehaviour(new FaceBehaviourImpl(bodyComponent.body, clickTarget.bodyComponent.body));
    }
    else {
      clickTarget.update(worldCoordinates);
    }
    steerableComponent.setEnabled(true);
  }

  public class ClickTarget extends GameEntity {
    BodyComponent bodyComponent;
    SteerableComponent steerableComponent;

    ClickTarget(Vector2 worldCoordinates) {
      bodyComponent = ComponentFactory.addBodyComponent(this, Box2dUtil.clickBody(worldCoordinates));
      steerableComponent = ComponentFactory.addSteerableComponent(this, bodyComponent.body, new SteeringData());
      ComponentFactory.addPlayerTargetCollisionComponent(this);
    }

    void update(Vector2 worldCoordinates) {
      bodyComponent.setWorldPosition(worldCoordinates);
    }
  }

}
