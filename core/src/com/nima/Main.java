package com.nima;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.model.Game;
import com.nima.render.ActorBasedTiledMultiMapRenderer;
import com.nima.model.SpineMainActor;
import com.nima.util.Resources;
import com.nima.util.Settings;

public class Main extends ApplicationAdapter {
  private OrthographicCamera camera;
  private ActorBasedTiledMultiMapRenderer tiledMapRenderer;
  private MainInputProcessor inputProcessor = new MainInputProcessor();

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.update();

    tiledMapRenderer = new Game(camera, Settings.GROUND_LAYER, Resources.MAIN_MAP_FOLDER, Resources.MAIN_MAP_PREFIX);
    tiledMapRenderer.setMainActor(new SpineMainActor(tiledMapRenderer, Resources.ACTOR_SPINE, "walk", 0.3f, 400, 400));
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
  }

}