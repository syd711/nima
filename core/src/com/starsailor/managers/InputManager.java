package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.GameState;
import com.starsailor.actors.Player;
import com.starsailor.util.Debugger;
import com.starsailor.util.GraphicsUtil;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {

  private OrthographicCamera camera;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private Game game;

  public InputManager(Game game, OrthographicCamera camera) {
    this.game = game;
    this.camera = camera;
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
    else if(keycode == Input.Keys.D) {
      Debugger.log();
      return true;
    }
    else if(keycode == Input.Keys.Q) {
      Game.gameSettings.save();
      return true;
    }
    else if(keycode == Input.Keys.C) {
      CameraManager.getInstance().reset();
      return true;
    }
    else if(keycode == Input.Keys.PLUS) {
      CameraManager.getInstance().updateTargetZoom(-0.2f);
      return true;
    }
    else if(keycode == Input.Keys.MINUS) {
      CameraManager.getInstance().updateTargetZoom(0.2f);
      return true;
    }
    else if(keycode == Input.Keys.P) {
      if(Game.gameState.getCurrentState().equals(GameState.PAUSED)) {
        game.resume();
      }
      else {
        game.pause();
      }

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
    float targetX = screenX;
    float targetY = Gdx.graphics.getHeight() - screenY;

    if(button == Input.Buttons.RIGHT) {
      Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
      Player.getInstance().moveTo(worldCoordinates);
      return true;
    }

    if(button == Input.Buttons.LEFT) {
      return SelectionManager.getInstance().selectAt(targetX, targetY, true);
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
