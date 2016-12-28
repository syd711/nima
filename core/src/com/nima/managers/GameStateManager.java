package com.nima.managers;

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
    EntityManager.getInstance().pauseSystems(true);
  }

  public boolean isStationMode() {
    return stationMode;
  }
}
