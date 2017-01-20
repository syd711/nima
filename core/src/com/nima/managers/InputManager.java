package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nima.Game;
import com.nima.actors.NPC;
import com.nima.actors.Player;
import com.nima.actors.states.PlayerState;
import com.nima.components.SelectionComponent;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.GraphicsUtil;
import com.nima.util.PolygonUtil;

import static com.nima.actors.states.PlayerState.IDLE;
import static com.nima.actors.states.PlayerState.MOVE_TO_STATION;

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
    else if(keycode == Input.Keys.S) {
      System.out.println(Player.getInstance().getStateMachine().getCurrentState());
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
        //first update the target to move to...
        float x = worldCoordinates.x;
        float y = worldCoordinates.y;
        Polygon clickPolygon = PolygonUtil.clickPolygon(worldCoordinates);
        TiledMultiMapRenderer.debugRenderer.render("click", clickPolygon);

        player.rotationComponent.setRotationTarget(x, y);

        //...then the speed to travel with
        Vector2 point1 = new Vector2(player.positionComponent.x, player.positionComponent.y);
        Vector2 point2 = new Vector2(x, y);
        player.speedComponent.applyTargetSpeed(point1, point2);
        return true;
      }

      if(button == Input.Buttons.MIDDLE) {
        Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
        player.fireAt(worldCoordinates);
      }

      if(button == Input.Buttons.LEFT) {
        return selectEntity(targetX, targetY, true);
      }
    }
    return false;
  }

  /**
   * Selected on or more entities
   * @param singleSelection true to disable all other selections.
   */
  private boolean selectEntity(float targetX, float targetY, boolean singleSelection) {
    Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
    Entity clickTarget = EntityManager.getInstance().getEntityAt(worldCoordinates.x, worldCoordinates.y);
    if(clickTarget instanceof NPC) {
      if(singleSelection) {
        ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(SelectionComponent.class);
        for(Entity entity : entitiesFor) {
          if(!entity.equals(clickTarget))
            entity.getComponent(SelectionComponent.class).selected = false;
        }
      }
      boolean selection = ((NPC)clickTarget).toggleSelection();
      if(selection) {
        Skin skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        Dialog dialog = new Dialog("Warning", skin) {
          public void result(Object obj) {
            System.out.println("result "+obj);
          }
        };
        dialog.text("Are you sure you want to quit?");
        dialog.button("Yes", true); //sends "true" as the result
        dialog.button("No", false);  //sends "false" as the result
        dialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        dialog.show(Game.hud.stage);
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
