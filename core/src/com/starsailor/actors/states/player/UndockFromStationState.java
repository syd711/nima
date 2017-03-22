package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.Player;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.InputManager;
import com.starsailor.systems.LightSystem;
import com.starsailor.ui.UIManager;

/**
 *
 */
public class UndockFromStationState implements State<Player> {
  @Override
  public void enter(Player player) {
    UIManager.getInstance().switchToHudState();

    player.scalingComponent.setTargetValue(1f);
    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(false);

    EntityManager.getInstance().pauseSystems(false);
    player.changeState(PlayerStates.IDLE);
  }

  @Override
  public void update(Player player) {
  }

  @Override
  public void exit(Player player) {
    InputManager.getInstance().setNavigationEnabled(true);
  }

  @Override
  public boolean onMessage(Player player, Telegram telegram) {
    return false;
  }
}
