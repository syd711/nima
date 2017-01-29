package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.util.GraphicsUtil;

/**
 * The player with all ashley components.
 */
public class Player extends Ship {
  private static Player instance = null;

  public Entity target;
  public Vector2 targetCoordinates;

  public static Player getInstance() {
    return instance;
  }

  public Player(ShipProfile profile) {
    super(profile);
    instance = this;
  }

  @Override
  public void createComponents(ShipProfile profile) {
    super.createComponents(profile);
    ComponentFactory.addPlayerCollisionComponent(this);

    //position player
    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
    bodyComponent.setWorldPosition(screenCenter);
  }

  public void moveTo(Vector2 worldCoordinates) {
    targetCoordinates = worldCoordinates;
    target = EntityManager.getInstance().getEntityAt(worldCoordinates);
    getStateMachine().changeState(PlayerStates.FOLLOW_CLICK);
    steerableComponent.setEnabled(true);
  }
}
