package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.GameStateManager;
import com.starsailor.actors.Player;
import com.starsailor.savegame.SaveGameManager;
import com.starsailor.ui.UIManager;
import com.starsailor.util.Debugger;
import com.starsailor.util.GraphicsUtil;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {

  private static InputManager instance = new InputManager();

  private OrthographicCamera camera;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private Vector2 lastClickLocation;
  private boolean navigationEnabled = true;

  private InputManager() {
    camera = Game.camera;
  }

  public static InputManager getInstance() {
    return instance;
  }

  public Vector2 getLastClickLocation() {
    return lastClickLocation;
  }

  public InputMultiplexer getInputMultiplexer() {
    return inputMultiplexer;
  }

  public void addInputProcessor(InputProcessor inputProcessor) {
    inputMultiplexer.addProcessor(inputProcessor);
  }

  public void setNavigationEnabled(boolean b) {
    this.navigationEnabled = b;
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    if(keycode == Input.Keys.ESCAPE) {
      com.starsailor.ui.UIManager.getInstance().switchToHudState();
    }
    else if(keycode == Input.Keys.F12) {
      System.exit(0);
    }
    else if(keycode == Input.Keys.I) {
      if(UIManager.getInstance().isInHudState()) {
        UIManager.getInstance().getHudStage().getIventoryPanel().toggle();
      }
      return true;
    }
    else if(keycode == Input.Keys.S) {
      SaveGameManager.save();
      return true;
    }
    else if(keycode == Input.Keys.D) {
      Debugger.log();
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
      GameStateManager.getInstance().setPaused(!GameStateManager.getInstance().isPaused());
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
    if(!navigationEnabled) {
      return false;
    }

    float targetX = screenX;
    float targetY = Gdx.graphics.getHeight() - screenY;

    lastClickLocation = new Vector2(targetX, targetY);
    Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
    float dst = worldCoordinates.dst(Player.getInstance().getCenter());
    if(dst < 80) {
      return false; //TODO
    }

    if(button == Input.Buttons.RIGHT) {
      Player.getInstance().moveTo(worldCoordinates);
      return true;
    }

    if(button == Input.Buttons.LEFT && !isScene2dClick()) {
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

  public boolean isScene2dClick() {
    if(UIManager.getInstance().getHudStage().getContextMenu().visible()) {
      return true;
    }

    return false;
  }
}
