package com.starsailor.ui.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.ui.stages.GameStage;

/**
 * Common UI state class
 */
public class UIState implements State<GameStage> {
  @Override
  public void enter(GameStage entity) {

  }

  @Override
  public void update(GameStage entity) {

  }

  @Override
  public void exit(GameStage entity) {

  }

  @Override
  public boolean onMessage(GameStage entity, Telegram telegram) {
    return false;
  }
}
