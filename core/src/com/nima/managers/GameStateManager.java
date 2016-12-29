package com.nima.managers;

import com.badlogic.gdx.Gdx;
import com.nima.Main;
import com.nima.actors.Player;

/**
 * Wraps the status, e.g. if paused
 */
public class GameStateManager {
  private final static GameStateManager INSTANCE = new GameStateManager();
  private boolean paused = false;
  private boolean navigating = true;
  private boolean inputBlocked = false;
  private boolean stationMode = false;

  private GameStateManager() {
    //force singleton
  }

  public static GameStateManager getInstance() {
    return INSTANCE;
  }

  public boolean isPaused() {
    return paused;
  }

  public void togglePause() {
    paused = !paused;
  }

  public boolean isNavigating() {
    return navigating;
  }

  public boolean isInputBlocked() {
    return inputBlocked;
  }

  public void setInputBlocked(boolean inputBlocked) {
    this.inputBlocked = inputBlocked;
  }

  public void enterStationMode() {
    stationMode = true;
    navigating = false;
    EntityManager.getInstance().pauseSystems(true);
  }

  public void leaveStationMode() {
    stationMode = false;
    navigating = true;
    Player player = EntityManager.getInstance().getPlayer();
    player.leaveStation();

    Gdx.input.setInputProcessor(Main.inputManager);
    EntityManager.getInstance().pauseSystems(false);
  }

  public boolean isStationMode() {
    return stationMode;
  }
}
