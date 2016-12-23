package com.nima;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.actors.Player;
import com.nima.hud.Hud;
import com.nima.managers.EntityManager;
import com.nima.managers.InputManager;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.Resources;
import com.nima.util.Settings;

public class Main extends ApplicationAdapter {
  private OrthographicCamera camera;
  private TiledMultiMapRenderer tiledMapRenderer;
  private InputManager inputManager;
  private Player player;

  //Ashley
  private PooledEngine engine = new PooledEngine();
  private EntityManager entityManager;

  //Box2d
  private World world;
  private Box2DDebugRenderer box2DDebugRenderer;

  //Scene2d
  private Hud hud;


  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    //camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.update();

    //box2d
    world = new World(new Vector2(0, 0), false);
    box2DDebugRenderer = new Box2DDebugRenderer();

    //map and player stuff
    tiledMapRenderer = new TiledMultiMapRenderer(Resources.MAIN_MAP_FOLDER, Resources.MAIN_MAP_PREFIX);
    entityManager = EntityManager.create(engine, tiledMapRenderer, world, camera);
    player = entityManager.getPlayer();

    //hud creation
    hud = new Hud();

    //input processing
    inputManager = new InputManager(player);
    Gdx.input.setInputProcessor(inputManager);
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
    update(Gdx.graphics.getDeltaTime());

    tiledMapRenderer.getBatch().end();

    inputManager.handleKeyInput();
    updateActorFrame();

    hud.render();
  }

  private void update(float deltaTime) {
    world.step(deltaTime, 6, 2);
//    box2DDebugRenderer.render(world, camera.combined);
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

  @Override
  public void dispose() {
    box2DDebugRenderer.dispose();
    world.dispose();
    super.dispose();
  }
}