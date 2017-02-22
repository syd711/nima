package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.Player;
import com.starsailor.managers.EntityManager;
import com.starsailor.systems.LightSystem;

/**
 *
 */
public class UndockFromStationState implements State<Player> {
  @Override
  public void enter(Player player) {
    player.scalingComponent.setTargetValue(1f);
    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(false);

    EntityManager.getInstance().pauseSystems(false);
    player.getStateMachine().changeState(PlayerState.IDLE);
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
