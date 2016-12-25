package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.nima.Main;
import com.nima.actors.Player;
import com.nima.components.MovementComponent;
import com.nima.components.PositionComponent;
import com.nima.util.GraphicsUtil;
import com.nima.util.Settings;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {

  private PositionComponent positionComponent;
  private final MovementComponent movementComponent;

  public InputManager(Player player) {
    this.positionComponent = player.getComponent(PositionComponent.class);
    movementComponent = player.getComponent(MovementComponent.class);
  }

  /**
   * Listening for key events for moving the character, etc.
   * Do not mix this with an InputProcessor which handles
   * single key events, e.g. open the map overview.
   */
  public void handleKeyInput() {
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      positionComponent.translate(-Settings.MAX_ACTOR_SPEED, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      positionComponent.translate(Settings.MAX_ACTOR_SPEED, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      positionComponent.translate(0, Settings.MAX_ACTOR_SPEED);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      positionComponent.translate(0, -Settings.MAX_ACTOR_SPEED);
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
    else if(keycode == Input.Keys.SPACE) {
      GameStateManager.getInstance().togglePause();
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
      float targetX = screenX;
      float targetY = Gdx.graphics.getHeight() - screenY;

      Entity target = EntityManager.getInstance().getEntityAt(targetX, targetY);
      if(target != null) {
        movementComponent.moveToEntity(target);
      }
      else {
        Vector2 mapTarget = GraphicsUtil.transform2WorldCoordinates(Main.camera, targetX, targetY);
        movementComponent.moveTo(mapTarget.x, mapTarget.y);
      }

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
