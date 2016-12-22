package com.nima;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.nima.actors.Player;
import com.nima.components.DimensionComponent;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;

/**
 * Created by Matthias on 16.12.2016.
 */
public class MainInputProcessor implements InputProcessor {

  private Player player;

  public MainInputProcessor(Player player) {
    this.player = player;
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
