package com.starsailor.ui.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.Player;
import com.starsailor.ui.UIManager;
import com.starsailor.ui.stages.GameStage;
import com.starsailor.util.box2d.Box2dUtil;

/**
 * Common UI state class
 */
public class UIState implements State<GameStage> {
  @Override
  public void enter(GameStage entity) {

  }

  @Override
  public void update(GameStage entity) {
    Body body = Player.getInstance().bodyShipComponent.body;
    boolean insideWorld = Box2dUtil.isInsideWorld(Game.world, body.getPosition());
    if(!insideWorld) {
      UIManager.getInstance().changeState(UIStates.NAVIGATION_STATE);
    }
  }

  @Override
  public void exit(GameStage entity) {

  }

  @Override
  public boolean onMessage(GameStage entity, Telegram telegram) {
    return false;
  }
}
