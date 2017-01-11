package com.nima.actors.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.nima.Game;
import com.nima.actors.Player;
import com.nima.components.MapObjectComponent;
import com.nima.managers.EntityManager;
import com.nima.systems.LightSystem;
import com.nima.util.Settings;

/**
 * The different states of an attack.
 */
public enum PlayerState implements State<Player> {

  IDLE() {
    @Override
    public void enter(Player player) {
      player.rotationComponent.mapTargetX = -1;
      player.rotationComponent.mapTargetY = -1;
    }

    @Override
    public void update(Player player) {
      float x = player.rotationComponent.mapTargetX;
      float y = player.rotationComponent.mapTargetY;
      if(x > 0 && y > 0) {
        Entity targetEntity = EntityManager.getInstance().getEntityAt(x, y);
        if(targetEntity != null) {
          player.target = targetEntity;
          player.getStateMachine().changeState(PlayerState.MOVE_TO_STATION);
        }
        else {
          player.target = null;
        }
      }
    }
  },
  MOVE_TO_STATION() {
    @Override
    public void enter(Player player) {
      MapObjectComponent mapObjectComponent = player.target.getComponent(MapObjectComponent.class);
      Vector2 centeredPosition = mapObjectComponent.getCenteredPosition();
      player.rotationComponent.setRotationTarget(centeredPosition.x, centeredPosition.y);
    }

    @Override
    public void update(Player player) {
      updateState(player);
    }
  },
  DOCK_TO_STATION() {
    @Override
    public void enter(Player player) {
      PlayerState previousState = player.getStateMachine().getPreviousState();
      if(previousState.equals(MOVE_TO_STATION)) {
        player.speedComponent.setTargetValue(1.5f);
        player.scalingComponent.setTargetValue(Settings.DOCKING_TARGET_SCALE);
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
      player.speedComponent.setTargetValue(0f);
      player.rotationComponent.setRotationTarget(player.positionComponent.x + 100, player.positionComponent.y);

      Gdx.input.setInputProcessor(Game.inputManager);
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
