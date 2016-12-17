package com.nima.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.render.ActorBasedTiledMultiMapRenderer;
import com.nima.util.Settings;

/**
 * The actual game renderer.
 * The ActorCenteredTiledMultiMapRenderer does most of the rendering magic
 * so that we simple have implement additional rendering log besides the
 * automatically multi map loading.
 */
public class Game extends ActorBasedTiledMultiMapRenderer {
  private OrthographicCamera camera;

  public Game(OrthographicCamera camera, String actorLayerName, String mapFolder, String mapPrefix) {
    super(actorLayerName, mapFolder, mapPrefix);
    this.camera = camera;
  }


  public void updateGameWorld() {
    handleKeyInput();
  }

  /**
   * Listening for key events for moving the character, etc.
   * Do not mix this with an InputProcessor which handles
   * single key events, e.g. open the map overview.
   */
  private void handleKeyInput() {
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      mainActor.moveBy(-Settings.ACTOR_VELOCITY, 0);
      camera.translate(-Settings.ACTOR_VELOCITY, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      mainActor.moveBy(Settings.ACTOR_VELOCITY, 0);
      camera.translate(Settings.ACTOR_VELOCITY, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      mainActor.moveBy(0, Settings.ACTOR_VELOCITY);
      camera.translate(0, Settings.ACTOR_VELOCITY);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      mainActor.moveBy(0, -Settings.ACTOR_VELOCITY);
      camera.translate(0, -Settings.ACTOR_VELOCITY);
    }
  }
}
