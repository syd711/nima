package com.starsailor.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.ScreenPositionComponent;
import com.starsailor.data.ShipProfile;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SelectionManager;
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
    super("Player", profile, null);
    instance = this;
  }

  @Override
  public void createComponents(Fraction fraction) {
    super.createComponents(fraction);
    ComponentFactory.addPlayerCollisionComponent(this);

    //make player always a member of the formationComponent, maybe we get a fellow someday?
    formationComponent.formationOwner = this;

    //position player
    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    add(new ScreenPositionComponent(screenCenter.x, screenCenter.y));
    bodyComponent.setWorldPosition(screenCenter);
  }

  @Override
  protected State getDefaultState() {
    return null;
  }

  @Override
  public void applyDamageFor(Bullet bullet) {
    updateDamage(bullet);
    if(SelectionManager.getInstance().getSelection() == null) {
      SelectionManager.getInstance().setSelection((Selectable) bullet.owner);
    }
  }

  /**
   * Moves to the given world coordinates
   * @param worldCoordinates the coordinates the user has clicked at
   */
  public void moveTo(Vector2 worldCoordinates) {
    targetCoordinates = worldCoordinates;
    target = EntityManager.getInstance().getEntityAt(worldCoordinates);
    getStateMachine().changeState(PlayerStates.FOLLOW_CLICK);
    steerableComponent.setEnabled(true);
  }
}
