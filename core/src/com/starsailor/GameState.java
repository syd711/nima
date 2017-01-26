package com.starsailor;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

/**
 * The game states
 */
public class GameState {
  
  public static State RESUME = new State<Game>() {
    @Override
    public void enter(Game game) {
      game.resume();
    }

    @Override
    public void update(Game game) {

    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public boolean onMessage(Game game, Telegram telegram) {
      return false;
    }
  };
  
  public static State PAUSED = new State<Game>() {
    @Override
    public void enter(Game game) {
      game.pause();
    }

    @Override
    public void update(Game game) {

    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public boolean onMessage(Game game, Telegram telegram) {
      return false;
    }
  };
  
}
