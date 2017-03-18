package com.starsailor.ui.states;

import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.GameStateManager;
import com.starsailor.actors.Player;
import com.starsailor.managers.SelectionManager;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.ui.stages.hud.HudStage;
import com.starsailor.util.box2d.Box2dUtil;

/**
 *
 */
public class DefaultState extends UIState {
  @Override
  public void enter(GameStage entity) {
    HudStage hudStage = UIManager.getInstance().getHudStage();
    hudStage.getContextMenu().hide();

    GameStateManager.getInstance().setPaused(false);
    SelectionManager.getInstance().resetSelection();
    Player.getInstance().switchToDefaultState();
  }

  @Override
  public void update(GameStage entity) {
    Body body = Player.getInstance().shipBodyComponent.body;
    boolean insideWorld = Box2dUtil.isInsideWorld(Game.world, body.getPosition());
    if(!insideWorld) {
      UIManager.getInstance().changeState(UIStates.NAVIGATION_STATE);
    }
  }
}
