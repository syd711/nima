package com.nima.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.states.PlayerState;
import com.nima.components.ComponentFactory;
import com.nima.components.MovementComponent;
import com.nima.components.ScreenPositionComponent;
import com.nima.data.ShipProfile;
import com.nima.util.GraphicsUtil;

/**
 * The player with all ashley components.
 */
public class Player extends Ship implements Updateable {
  protected MovementComponent movementComponent;

  private static Player instance = null;
  public StateMachine<Player, PlayerState> stateMachine;

  public static Player getInstance() {
    return instance;
  }

  public Player(ShipProfile profile) {
    super(profile);
    instance = this;
  }

  @Override
  protected void createComponents(ShipProfile profile) {
    super.createComponents(profile);

    //position player
    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
    positionComponent.setPosition(screenCenter);

    //special player movement
    movementComponent = ComponentFactory.addMovementComponent(this);

    //state machine for the player
    stateMachine = new DefaultStateMachine<>(this, PlayerState.IDLE);
  }

  @Override
  public void update() {
    stateMachine.update();
  }
}
