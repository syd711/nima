package com.starsailor.actors.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Location;
import com.starsailor.actors.Player;
import com.starsailor.components.MapObjectComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.systems.LightSystem;
import com.starsailor.util.Settings;

/**
 * The different states of an attack.
 */
public enum PlayerState implements State<Player> {

  IDLE() {
    @Override
    public void enter(Player player) {
    }

    @Override
    public void update(Player player) {
      Entity targetEntity = EntityManager.getInstance().getEntityAt(player.getCenter());
      if(targetEntity != null && targetEntity instanceof Location) {
        player.target = targetEntity;
        player.getStateMachine().changeState(PlayerState.MOVE_TO_STATION);
      }
      else {
        player.target = null;
      }
    }
  },
  ATTACKED() {
    @Override
    public void enter(Player entity) {
      super.enter(entity);
    }

    @Override
    public void update(Player entity) {

    }
  },
  ATTACK() {
    @Override
    public void update(Player entity) {

    }
  },
  MOVE_TO_STATION() {
    @Override
    public void enter(Player player) {
      MapObjectComponent mapObjectComponent = player.target.getComponent(MapObjectComponent.class);
      Vector2 centeredPosition = mapObjectComponent.getCenteredPosition();
      player.moveTo(centeredPosition);
    }

    @Override
    public void update(Player player) {
      updateState(player);
    }
  },
  DOCK_TO_STATION() {
    @Override
    public void enter(Player player) {
      PlayerState previousState = (PlayerState) player.getStateMachine().getPreviousState();
      if(previousState.equals(MOVE_TO_STATION)) {
        player.scalingComponent.setTargetValue(Settings.getInstance().docking_target_scale);
        LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
        lightSystem.fadeOut(true);
      }
    }

    @Override
    public void update(Player player) {
      LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
      if(lightSystem.isOutFaded() && !player.scalingComponent.isChanging()) {
        player.getStateMachine().changeState(DOCKED);
      }
    }
  },
  DOCKED() {
    @Override
    public void enter(Player player) {
      player.target = null;
      EntityManager.getInstance().pauseSystems(true);
    }

    @Override
    public void update(Player player) {
      updateState(player);
    }
  },
  UNDOCK_FROM_STATION() {
    @Override
    public void enter(Player player) {
      player.scalingComponent.setTargetValue(1f);
      LightSystem lightSystem = EntityManager.getInstance().getLightSystem();
      lightSystem.fadeOut(false);

      EntityManager.getInstance().pauseSystems(false);
      player.getStateMachine().changeState(IDLE);
    }

    @Override
    public void update(Player player) {
      updateState(player);
    }
  };

  @Override
  public void enter(Player entity) {

  }

  @Override
  public void exit(Player entity) {

  }

  @Override
  public boolean onMessage(Player entity, Telegram telegram) {
    return false;
  }

  // ------------------- Helper -----------------------------------
  private static void updateState(Player player) {

  }
}
