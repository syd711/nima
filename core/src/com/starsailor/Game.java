package com.starsailor;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.starsailor.actors.Player;
import com.starsailor.components.PositionComponent;
import com.starsailor.managers.*;
import com.starsailor.render.TiledMultiMapRenderer;
import com.starsailor.render.converters.*;
import com.starsailor.ui.Hud;
import com.starsailor.util.GameSettings;
import com.starsailor.util.Resources;
import com.starsailor.util.Settings;

public class Game extends ApplicationAdapter {
  public static long currentTimeMillis;

  public static OrthographicCamera camera;
  private RayHandler rayHandler;
  private TiledMultiMapRenderer tiledMapRenderer;
  public static InputManager inputManager;
  private PositionComponent positionComponent;

  //Ashley
  private EntityManager entityManager;

  //Box2d
  public static World world;
  private Box2DDebugRenderer box2DDebugRenderer;

  //Scene2d
  public static Hud hud;

  private SpriteBatch batch;

  public static GameSettings gameSettings = GameSettings.load();

  public static DefaultStateMachine gameState;

  private boolean paused;

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    gameState = new DefaultStateMachine(this, GameState.RESUME);

    //load particle effects
    ParticleManager.getInstance().loadParticles();

    //load sprites
    TextureManager.getInstance().loadTextures();

    //camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    camera.update();

    //box2d
    world = new World(new Vector2(0, 0), false);
    world.setContactListener(new GameContactListener());
    box2DDebugRenderer = new Box2DDebugRenderer();
    batch = new SpriteBatch();

    //light
    rayHandler = new RayHandler(world);
    RayHandler.useDiffuseLight(true);
    rayHandler.setCulling(true);
    rayHandler.setCombinedMatrix(camera);

    tiledMapRenderer = new TiledMultiMapRenderer(Resources.MAIN_MAP_FOLDER, Resources.MAIN_MAP_PREFIX, batch);
    //Ashley Entity Engine
    entityManager = EntityManager.create(tiledMapRenderer, camera, rayHandler);

    //Initializing the game by a full map scan
    tiledMapRenderer.addMapObjectConverter(new MapObjectPositionUpdateConverter());
    tiledMapRenderer.addMapObjectConverter(new MapObjectPositionConverter());
    tiledMapRenderer.addMapObjectConverter(new MapObjectCenteredPositionConverter());
    tiledMapRenderer.addMapObjectConverter(new Route2EntityConverter(Settings.getInstance().npcs_enabled));
    tiledMapRenderer.fullScan(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);
    tiledMapRenderer.removeAllObjectConverters();


    //Create the multi map renderer + converters, keep order!
    tiledMapRenderer.addMapObjectConverter(new MapObjectPositionUpdateConverter());
    tiledMapRenderer.addMapObjectConverter(new MapObjectPositionConverter());
    tiledMapRenderer.addMapObjectConverter(new MapObjectCenteredPositionConverter());
    tiledMapRenderer.addMapObjectConverter(new MapObjectBox2dConverter(world));
    //this will add Ashley entities!
    tiledMapRenderer.addMapObjectConverter(new MapObject2ConeLightConverter(rayHandler));
    tiledMapRenderer.addMapObjectConverter(new MapObject2PointLightConverter(rayHandler));
    tiledMapRenderer.addMapObjectConverter(new MapObject2StationEntityConverter());

    //init player
    Player player = entityManager.getPlayer();
    positionComponent = player.getComponent(PositionComponent.class);

    //input processing
    inputManager = new InputManager(player, camera);

    //hud creation
    hud = new Hud();

    //add the inputmanager itself as input processor, but as last!
    inputManager.getInputMultiplexer().addProcessor(inputManager);
    Gdx.input.setInputProcessor(inputManager.getInputMultiplexer());

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        gameSettings.save();
      }
    });
  }

  @Override
  public void pause() {
    paused = true;
  }

  @Override
  public void resume() {
    paused = false;
  }

  int debugRender = 0;

  @Override
  public void render() {
    currentTimeMillis = System.currentTimeMillis();

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

    entityManager.update();

    tiledMapRenderer.getBatch().begin();
    float deltaTime = Gdx.graphics.getDeltaTime();
    world.step(deltaTime, 6, 2);

    if(Settings.getInstance().debug) {
      debugRender++;
      if(debugRender % 2 == 0) {
        debugRender = 0;
        box2DDebugRenderer.render(world, debugMatrix);
      }
    }

    tiledMapRenderer.getBatch().end();
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
    SoundManager.dispose();
    box2DDebugRenderer.dispose();
    world.dispose();
    super.dispose();
  }
}