package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.Player;
import com.starsailor.GameStateManager;
import com.starsailor.ui.UIManager;

/**
 *
 */
public class DockedState implements State<Player> {
  @Override
  public void enter(Player player) {
    GameStateManager.getInstance().setPaused(true);
    UIManager.getInstance().switchToLocationState();
  }

  @Override
  public void update(Player player) {
  }

  @Override
  public void exit(Player player) {

  }

  @Override
  public boolean onMessage(Player player, Telegram telegram) {
    return false;
  }
}
