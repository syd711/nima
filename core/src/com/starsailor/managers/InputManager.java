package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.Player;
import com.starsailor.actors.states.PlayerState;
import com.starsailor.util.GraphicsUtil;

import static com.starsailor.actors.states.PlayerState.IDLE;
import static com.starsailor.actors.states.PlayerState.MOVE_TO_STATION;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {

  private OrthographicCamera camera;
  private Player player;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  public InputManager(Player player, OrthographicCamera camera) {
    this.camera = camera;
    this.player = player;
  }

  public InputMultiplexer getInputMultiplexer() {
    return inputMultiplexer;
  }

  public void addInputProcessor(InputProcessor inputProcessor) {
    inputMultiplexer.addProcessor(inputProcessor);
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
    else if(keycode == Input.Keys.S) {
      System.out.println(Player.getInstance().getStateMachine().getCurrentState());
      return true;
    }
    else if(keycode == Input.Keys.Q) {
      Game.gameSettings.save();
      return true;
    }
    else if(keycode == Input.Keys.NUM_1) {
      Player.getInstance().switchWeapon(1);
      return true;
    }
    else if(keycode == Input.Keys.NUM_2) {
      Player.getInstance().switchWeapon(2);
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
    PlayerState currentState = (PlayerState) player.getStateMachine().getCurrentState();
    float targetX = screenX;
    float targetY = Gdx.graphics.getHeight() - screenY;

    if(currentState.equals(IDLE) || currentState.equals(MOVE_TO_STATION)) {
      if(button == Input.Buttons.RIGHT) {
        Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
        Player.getInstance().moveTo(worldCoordinates);
        return true;
      }

      if(button == Input.Buttons.LEFT) {
        return SelectionManager.getInstance().selectAt(targetX, targetY, true);
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
