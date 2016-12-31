package com.nima;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nima.actors.Player;
import com.nima.components.PositionComponent;
import com.nima.hud.Hud;
import com.nima.managers.EntityManager;
import com.nima.managers.InputManager;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.util.Resources;
import com.nima.util.Settings;

public class Game extends ApplicationAdapter {
  public static OrthographicCamera camera;
  private RayHandler rayHandler;
  private TiledMultiMapRenderer tiledMapRenderer;
  public static InputManager inputManager;
  private Player player;
  private PositionComponent positionComponent;

  //Ashley
  private PooledEngine engine = new PooledEngine();
  private EntityManager entityManager;

  //Box2d
  public static World world;
  private Box2DDebugRenderer box2DDebugRenderer;

  //Scene2d
  private static Hud hud;

  private final static int VIEWPORT_WIDTH = 16;
  private final static int VIEWPORT_HEIGHT = 9;

  private SpriteBatch batch;

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    //camera
    camera = new OrthographicCamera();
//    camera.position.set(VIEWPORT_WIDTH/2, VIEWPORT_HEIGHT, 0);
    camera.setToOrtho(false);
    camera.update();

    //box2d
    world = new World(new Vector2(0, 0), false);
    box2DDebugRenderer = new Box2DDebugRenderer();
    batch = new SpriteBatch();

    //light
    rayHandler = new RayHandler(world);
    RayHandler.useDiffuseLight(true);
    rayHandler.setCulling(true);
    rayHandler.setCombinedMatrix(camera);

    //map and player stuff
    tiledMapRenderer = new TiledMultiMapRenderer(world, Resources.MAIN_MAP_FOLDER, Resources.MAIN_MAP_PREFIX, batch);
    entityManager = EntityManager.create(engine, tiledMapRenderer, camera, rayHandler);
    player = entityManager.getPlayer();
    positionComponent = player.getComponent(PositionComponent.class);

    //hud creation
    hud = new Hud();

    //input processing
    inputManager = new InputManager(player, camera);
    Gdx.input.setInputProcessor(inputManager);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();

    Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(Settings.PPM, Settings.PPM, 0);
    batch.setProjectionMatrix(camera.combined);

    tiledMapRenderer.setView(camera);

    tiledMapRenderer.preRender();
    tiledMapRenderer.render();
    tiledMapRenderer.postRender();

    tiledMapRenderer.getBatch().begin();
    entityManager.update();
    float deltaTime = Gdx.graphics.getDeltaTime();
    world.step(deltaTime, 6, 2);
    box2DDebugRenderer.render(world, debugMatrix);

    tiledMapRenderer.getBatch().end();

    inputManager.handleKeyInput();
    updateActorFrame();

    rayHandler.setCombinedMatrix(camera);
    rayHandler.updateAndRender();

    //hud overlay at last
    hud.render();
  }

  /**
   * Notifies the map renderer on which map
   * the player is currently on.
   */
  private void updateActorFrame() {
    float x = positionComponent.x;
    int actorFrameX = (int) (x / Settings.FRAME_PIXELS_X);
    float y = positionComponent.y;
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