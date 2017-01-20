package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.PlayerState;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.MovementComponent;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.util.GraphicsUtil;

/**
 * The player with all ashley components.
 */
public class Player extends Ship {
  protected MovementComponent movementComponent;

  private static Player instance = null;
  public Entity target;

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
    positionComponent.setPosition(screenCenter);

    //special player movement
    movementComponent = ComponentFactory.addMovementComponent(this);
  }

}
