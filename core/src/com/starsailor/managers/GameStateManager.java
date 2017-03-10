package com.starsailor.managers;

/**
 * Wraps the status, e.g. if paused
 */
//TODO use for stations
public class GameStateManager {
  private final static GameStateManager INSTANCE = new GameStateManager();
  private boolean navigating = true;
  private boolean stationMode = false;

  private GameStateManager() {
    //force singleton
  }

  public static GameStateManager getInstance() {
    return INSTANCE;
  }


  public boolean isNavigating() {
    return navigating;
  }

  public boolean isStationMode() {
    return stationMode;
  }
}
