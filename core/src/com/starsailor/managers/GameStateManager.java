package com.starsailor.managers;

import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.starsailor.GameState;

/**
 * Wraps the status, e.g. if paused
 */
public class GameStateManager {
  private final static GameStateManager INSTANCE = new GameStateManager();

  public StateMachine gameState;

  private GameStateManager() {
    gameState = new StackStateMachine<>(this, GameState.RESUME);
  }

  public static GameStateManager getInstance() {
    return INSTANCE;
  }

  public void setPaused(boolean b) {
    if(b) {
      gameState.changeState(GameState.PAUSED);
    }
    else {
      gameState.changeState(GameState.RESUME);
    }
  }

  public boolean isPaused() {
    return gameState.getCurrentState().equals(GameState.PAUSED);
  }

}
