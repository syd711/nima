package com.nima.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.nima.actors.Player;
import com.nima.util.GraphicsUtil;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {

  private OrthographicCamera camera;
  private Player player;

  public InputManager(Player player, OrthographicCamera camera) {
    this.camera = camera;
    this.player = player;
  }

  /**
   * Listening for key events for moving the character, etc.
   * Do not mix this with an InputProcessor which handles
   * single key events, e.g. open the map overview.
   */
  public void handleKeyInput() {
//    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//      positionComponent.translate(-Settings.MAX_ACTOR_SPEED, 0);
//    }
//    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//      positionComponent.translate(Settings.MAX_ACTOR_SPEED, 0);
//    }
//    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
//      positionComponent.translate(0, Settings.MAX_ACTOR_SPEED);
//    }
//    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//      positionComponent.translate(0, -Settings.MAX_ACTOR_SPEED);
//    }
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
    else if(keycode == Input.Keys.T) {
      return true;
    }

    return false;
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
    if(GameStateManager.getInstance().isNavigating()) {
      if(button == Input.Buttons.LEFT) {
        float targetX = screenX;
        float targetY = Gdx.graphics.getHeight() - screenY;

        Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
        //first update the target to move to...
        float x = worldCoordinates.x;
        float y = worldCoordinates.y;

        player.rotationComponent.setRotationTarget(x, y);

        //...then the speed to travel with
        Vector2 point1 = new Vector2(player.positionComponent.x, player.positionComponent.y);
        Vector2 point2 = new Vector2(x, y);
        player.speedComponent.applyTargetSpeed(point1, point2);
        return true;
      }

      if(button == Input.Buttons.RIGHT) {
        float targetX = screenX;
        float targetY = Gdx.graphics.getHeight() - screenY;
        Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
        player.fireAt(worldCoordinates);
      }
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
