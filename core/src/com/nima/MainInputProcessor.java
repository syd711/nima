package com.nima;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.nima.components.DimensionComponent;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;

/**
 * Created by Matthias on 16.12.2016.
 */
public class MainInputProcessor implements InputProcessor {

  private PositionComponent playerPosition;
  private DimensionComponent playerDimension;
  private SpineComponent player;

  public MainInputProcessor(SpineComponent player, PositionComponent playerPosition, DimensionComponent playerDimension) {
    this.player = player;
    this.playerDimension = playerDimension;
    this.playerPosition = playerPosition;
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
      double angleDeg = Math.atan2(screenY - playerPosition.y+playerDimension.height/2, screenX - playerPosition.x+playerDimension.width/2) * 180 / Math.PI;
      player.setRotation((float) angleDeg);
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
