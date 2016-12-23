package com.nima.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.nima.actors.Player;
import com.nima.util.Settings;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {

  private Player player;

  public InputManager(Player player) {
    this.player = player;
  }

  /**
   * Listening for key events for moving the character, etc.
   * Do not mix this with an InputProcessor which handles
   * single key events, e.g. open the map overview.
   */
  public void handleKeyInput() {
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      player.getPositionComponent().translate(-Settings.ACTOR_DEFAULT_SPEED, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      player.getPositionComponent().translate(Settings.ACTOR_DEFAULT_SPEED, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      player.getPositionComponent().translate(0, Settings.ACTOR_DEFAULT_SPEED);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      player.getPositionComponent().translate(0, -Settings.ACTOR_DEFAULT_SPEED);
    }
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    if(keycode == Input.Keys.ESCAPE) {
      System.exit(0);
    }
    return true;
  }

  @Override
  public boolean keyTyped(char character) {

    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      player.moveTo(screenX, screenY);
      return true;
    }
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }
}
