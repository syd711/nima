package com.nima.managers;

/**
 * Wraps the status, e.g. if paused
 */
public class GameStateManager {
  private final static GameStateManager INSTANCE = new GameStateManager();

  private boolean paused;
  private boolean inGameMenu;

  private GameStateManager() {
    //force singleton
  }

  public static GameStateManager getInstance() {
    return INSTANCE;
  }

  public boolean isPaused() {
    return paused;
  }

  public void setPaused(boolean paused) {
    this.paused = paused;
  }

  public void togglePause() {
    this.paused = !paused;
  }

  public boolean isInGameMenu() {
    return inGameMenu;
  }

  public void setInGameMenu(boolean inGameMenu) {
    this.inGameMenu = inGameMenu;
  }
}
