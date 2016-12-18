package com.nima;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.model.Game;
import com.nima.model.SpineMainActor;
import com.nima.render.ActorBasedTiledMultiMapRenderer;
import com.nima.util.Resources;
import com.nima.util.Settings;

public class Main extends ApplicationAdapter {
  private OrthographicCamera camera;
  private ActorBasedTiledMultiMapRenderer tiledMapRenderer;
  private MainInputProcessor inputProcessor = new MainInputProcessor();
  private SpineMainActor mainActor;

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.update();

    tiledMapRenderer = new Game(camera, Settings.ACTOR_LAYER, Resources.MAIN_MAP_FOLDER, Resources.MAIN_MAP_PREFIX);
    mainActor = new SpineMainActor(tiledMapRenderer, Resources.ACTOR_SPINE, "walk", 0.3f);

    tiledMapRenderer.setMainActor(mainActor);
    Gdx.input.setInputProcessor(inputProcessor);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();
    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();

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
//      tiledMapRenderer.updateCamera(camera);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      mainActor.moveBy(Settings.ACTOR_VELOCITY, 0);
//      tiledMapRenderer.updateCamera(camera);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      mainActor.moveBy(0, Settings.ACTOR_VELOCITY);
//      tiledMapRenderer.updateCamera(camera);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      mainActor.moveBy(0, -Settings.ACTOR_VELOCITY);
//      tiledMapRenderer.updateCamera(camera);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
      camera.zoom = 0.5f;
    }
  }
}