package com.nima;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.actors.Player;
import com.nima.managers.EntityManager;
import com.nima.render.DebugRenderer;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.Resources;
import com.nima.util.Settings;

public class Main extends ApplicationAdapter {
  private OrthographicCamera camera;
  public TiledMultiMapRenderer tiledMapRenderer;
  private MainInputProcessor inputProcessor;
  private Player player;

  public static DebugRenderer DEBUG_RENDERER;

  //Ashley
  private PooledEngine engine = new PooledEngine();
  private EntityManager entityManager;

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.update();

    tiledMapRenderer = new TiledMultiMapRenderer(Resources.MAIN_MAP_FOLDER, Resources.MAIN_MAP_PREFIX);
    DEBUG_RENDERER = new DebugRenderer(tiledMapRenderer, camera);
    entityManager = EntityManager.create(engine, tiledMapRenderer, camera);

    this.player = entityManager.getPlayer();

    inputProcessor = new MainInputProcessor(player);
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

    tiledMapRenderer.getBatch().begin();
    entityManager.update();
//    DEBUG_RENDERER.render();
    tiledMapRenderer.getBatch().end();

    handleKeyInput();
    updateActorFrame();
  }

  /**
   * Notifies the map renderer on which map
   * the player is currently on.
   */
  private void updateActorFrame() {
    float x = player.getX();
    int actorFrameX = (int) (x / Settings.FRAME_PIXELS_X);
    float y = player.getY();
    int actorFrameY = (int) (y / Settings.FRAME_PIXELS_Y);
    tiledMapRenderer.setActorFrame(actorFrameX, actorFrameY);
  }

  /**
   * Listening for key events for moving the character, etc.
   * Do not mix this with an InputProcessor which handles
   * single key events, e.g. open the map overview.
   */
  private void handleKeyInput() {
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      player.getPositionComponent().translate(-Settings.ACTOR_VELOCITY, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      player.getPositionComponent().translate(Settings.ACTOR_VELOCITY, 0);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      player.getPositionComponent().translate(0, Settings.ACTOR_VELOCITY);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      player.getPositionComponent().translate(0, -Settings.ACTOR_VELOCITY);
    }
    if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
      camera.zoom = 0.5f;
    }
  }
}