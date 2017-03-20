package com.starsailor.actors.states.player;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.Game;
import com.starsailor.actors.Player;
import com.starsailor.managers.EntityManager;
import com.starsailor.systems.LightSystem;
import com.starsailor.util.Settings;

/**
 *
 */
public class DockToStationState implements State<Player> {
  @Override
  public void enter(Player player) {
    Game.inputManager.setNavigationEnabled(false);
    player.scalingComponent.setTargetValue(Settings.getInstance().docking_target_scale);
    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    lightSystem.fadeOut(true);
  }

  @Override
  public void update(Player player) {
    LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
    if(lightSystem.isOutFaded() && !player.scalingComponent.isChanging()) {
      player.getStateMachine().changeState(PlayerStates.DOCKED);
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
