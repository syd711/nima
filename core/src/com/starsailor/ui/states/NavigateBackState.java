package com.starsailor.ui.states;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.Galaxy;
import com.starsailor.actors.Player;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.managers.InputManager;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.util.box2d.Box2dUtil;

/**
 * Forces the player to move back into the world if the end is near.
 */
public class NavigateBackState extends UIState {
  private Vector2 worldExitPoint;

  @Override
  public void enter(GameStage entity) {
    UIManager.getInstance().getHudStage().getNavigationPanel().deactivate();
    InputManager.getInstance().setNavigationEnabled(false);

    worldExitPoint = Player.getInstance().getCenter();
    Player.getInstance().moveTo(Galaxy.getInstance().getCenter());
  }

  @Override
  public void update(GameStage entity) {
    boolean insideWorld = Box2dUtil.isInsideWorld(Game.world, Player.getInstance().bodyShipComponent.body.getPosition());
    if(insideWorld) {
      float dst = worldExitPoint.dst(Player.getInstance().getCenter());
      if(dst > 100) {
        Player.getInstance().changeState(PlayerStates.IDLE);
        UIManager.getInstance().changeState(UIStates.IDLE_STATE);
      }
    }
  }

  @Override
  public void exit(GameStage entity) {
    InputManager.getInstance().setNavigationEnabled(true);
  }
}
