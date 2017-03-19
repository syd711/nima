package com.starsailor.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.Galaxy;
import com.starsailor.actors.Player;
import com.starsailor.components.PositionComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.render.converters.*;
import com.starsailor.util.Settings;

import java.util.ArrayList;
import java.util.List;

import static com.starsailor.util.Settings.PPM;

/**
 * Responsible for the different tmx maps.
 */
public class MapManager {
  private static MapManager instance = new MapManager();

  private List<MapObjectConverter> offScreenConverters = new ArrayList<>();
  private List<MapObjectConverter> onScreenConverters = new ArrayList<>();

  private SpriteBatch batch;
  private TiledMultiMapRenderer tiledMapRenderer;

  public static MapManager getInstance() {
    return instance;
  }

  private MapManager() {
    batch = new SpriteBatch();

    //Initializing the map by a full map scan
    offScreenConverters.add(new MapObjectPositionUpdateConverter());
    offScreenConverters.add(new MapObjectPositionConverter());
    offScreenConverters.add(new MapObjectCenteredPositionConverter());
    offScreenConverters.add(new Route2EntityConverter(Settings.getInstance().npcs_enabled));

    //Create the multi map renderer + converters, keep order!
    onScreenConverters.add(new MapObjectPositionUpdateConverter());
    onScreenConverters.add(new MapObjectPositionConverter());
    onScreenConverters.add(new MapObjectCenteredPositionConverter());
    onScreenConverters.add(new MapObjectBox2dConverter(Game.world));
    //this will add Ashley entities!
    onScreenConverters.add(new MapObject2ConeLightConverter(Game.rayHandler));
    onScreenConverters.add(new MapObject2PointLightConverter(Game.rayHandler));
    onScreenConverters.add(new MapObject2StationEntityConverter());
    onScreenConverters.add(new MapObject2ShipConverter(Settings.getInstance().npcs_enabled));
  }

  /**
   * Loads a new map with all map converters.
   * @param name the name of the map to load.
   */
  public void loadMap(String name) {

    tiledMapRenderer = new TiledMultiMapRenderer(name, batch);

    //run initial global scans
    for(MapObjectConverter offScreenConverter : offScreenConverters) {
      tiledMapRenderer.addMapObjectConverter(offScreenConverter);
    }
    tiledMapRenderer.fullScan();
    tiledMapRenderer.removeAllObjectConverters();

    //apply the converter to run each time a new map is loaded
    for(MapObjectConverter onScreenConverter : onScreenConverters) {
      tiledMapRenderer.addMapObjectConverter(onScreenConverter);
    }

    //apply the start positon of the player
    int startX = tiledMapRenderer.getStartX();
    int startY = tiledMapRenderer.getStartY();
    Player.getInstance().shipBodyComponent.setWorldPosition(new Vector2(startX, startY));

    Galaxy.getInstance().update();
  }

  /**
   * Rendering of the current map renderer.
   */
  public void update(float deltaTime) {
    Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0);
    batch.setProjectionMatrix(Game.camera.combined);

    tiledMapRenderer.setView(Game.camera);
    tiledMapRenderer.render();

    if(Settings.getInstance().debug) {
      tiledMapRenderer.getBatch().begin();
      Game.box2DDebugRenderer.render(Game.world, debugMatrix);
      tiledMapRenderer.getBatch().end();
    }

    PositionComponent positionComponent = Player.getInstance().positionComponent;
    tiledMapRenderer.setActorFragment( positionComponent.x, positionComponent.y);
  }

  public SpriteBatch getBatch() {
    return batch;
  }

  public TiledMultiMapRenderer getTiledMapRenderer() {
    return tiledMapRenderer;
  }
}
