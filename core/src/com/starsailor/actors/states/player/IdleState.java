package com.starsailor.actors.states.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.Location;
import com.starsailor.actors.Player;

/**
 *
 */
public class IdleState implements State<Player> {
  @Override
  public void enter(Player player) {

  }

  @Override
  public void update(Player player) {
    Entity targetEntity = player.target;
    if(targetEntity != null && targetEntity instanceof Location && !player.isInBattleState()) {
      player.getStateMachine().changeState(PlayerState.MOVE_TO_STATION);
    }
    else {
      player.target = null;
      player.steerableComponent.setBehavior(null);
    }
  }

  @Override
  public void exit(Player player) {

  }

  @Override
  public boolean onMessage(Player player, Telegram telegram) {
    return false;
  }
}
