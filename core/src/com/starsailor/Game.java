package com.starsailor;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.starsailor.managers.*;
import com.starsailor.render.MapManager;
import com.starsailor.render.TiledMultiMapRenderer;
import com.starsailor.ui.UIManager;
import com.starsailor.util.GameSettings;
import com.starsailor.util.GameTimer;
import com.starsailor.util.Settings;

public class Game extends ApplicationAdapter {
  public static long currentTimeMillis;

  public static OrthographicCamera camera;
  public static RayHandler rayHandler;
  public static TiledMultiMapRenderer tiledMapRenderer;

  //Box2d
  public static World world;
  public static Box2DDebugRenderer box2DDebugRenderer;

  public static GameSettings gameSettings = GameSettings.load();
  private Settings settings = Settings.getInstance();

  @Override
  public void create() {
    //Load assets
    ResourceManager.getInstance().loadAssets();

    GameDataManager.getInstance().load();

    //camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    camera.zoom = settings.cameraZoom;
    camera.update();

    //box2d
    world = new World(new Vector2(0, 0), false);
    world.setContactListener(new GameContactListener());
    box2DDebugRenderer = new Box2DDebugRenderer();

    //light
    rayHandler = new RayHandler(world);
    RayHandler.useDiffuseLight(true);
    rayHandler.setCulling(true);
    rayHandler.setCombinedMatrix(camera);

    //Ashley Entity Engine
    EntityManager.create();

    //load initial map
    MapManager.getInstance().loadMap("erebos");

    //init camera manager
    CameraManager.getInstance().init(camera);

    //add the inputmanager itself as input processor, but as last!
    InputManager.getInstance().getInputMultiplexer().addProcessor(InputManager.getInstance());
    Gdx.input.setInputProcessor(InputManager.getInstance().getInputMultiplexer());

    Pixmap pm = new Pixmap(Gdx.files.internal("textures/cursor.png"));
    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
    pm.dispose();

    //shutdown hook to store settings
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        gameSettings.save();
      }
    });
  }

  @Override
  public void pause() {
    if(!settings.debug) {
      GameStateManager.getInstance().setPaused(true);
    }
  }

  @Override
  public void resume() {
    GameStateManager.getInstance().setPaused(false);
  }

  @Override
  public void render() {
    currentTimeMillis = System.currentTimeMillis();
    float deltaTime = Gdx.graphics.getDeltaTime();

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    CameraManager.getInstance().update(deltaTime);
    BulletManager.getInstance().update(deltaTime);
    MessageManager.getInstance().update();
    MapManager.getInstance().update(deltaTime);

    rayHandler.setCombinedMatrix(camera);
    rayHandler.updateAndRender();

    //update timer including libgdx AI
    if(!GameStateManager.getInstance().isPaused()) {
      float update = GameTimer.update(deltaTime);
      world.step(update, 6, 2);
    }

    //update engine after world.step for saver body removal
    EntityManager.getInstance().update();

    //hud overlay at last
    UIManager.getInstance().update(deltaTime);
  }

  @Override
  public void dispose() {
    ResourceManager.getInstance().dispose();
    SoundManager.dispose();
    box2DDebugRenderer.dispose();
    world.dispose();
    super.dispose();
  }
}