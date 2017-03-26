package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.Player;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SelectionManager;

/**
 *
 */
public class PlayerIdleState implements State<Player> {
  @Override
  public void enter(Player player) {
    player.steerableComponent.setBehavior(null);
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
