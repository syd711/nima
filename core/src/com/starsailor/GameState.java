package com.starsailor;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.managers.GameStateManager;

/**
 * The game states
 */
public class GameState {
  
  public static State RESUME = new State<GameStateManager>() {
    @Override
    public void enter(GameStateManager gameStateManager) {

    }

    @Override
    public void update(GameStateManager gameStateManager) {

    }

    @Override
    public void exit(GameStateManager gameStateManager) {

    }

    @Override
    public boolean onMessage(GameStateManager gameStateManager, Telegram telegram) {
      return false;
    }
  };


  public static State PAUSED = new State<GameStateManager>() {
    @Override
    public void enter(GameStateManager gameStateManager) {
    }

    @Override
    public void update(GameStateManager gameStateManager) {

    }

    @Override
    public void exit(GameStateManager gameStateManager) {

    }

    @Override
    public boolean onMessage(GameStateManager gameStateManager, Telegram telegram) {
      return false;
    }
  };
  
}
