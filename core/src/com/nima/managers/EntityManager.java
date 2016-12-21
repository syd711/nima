package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nima.actors.Camera;
import com.nima.actors.MapObjectToEntityConverter;
import com.nima.actors.Updateable;
import com.nima.components.CollisionComponent;
import com.nima.components.DimensionComponent;
import com.nima.components.PositionComponent;
import com.nima.components.SpineComponent;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.systems.PlayerCollisionSystem;
import com.nima.systems.SpinePositionSystem;
import com.nima.systems.SpineRenderSystem;
import com.nima.util.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Central Ashley initialization of entity systems.
 */
public class EntityManager {
  private static final Logger LOG = Logger.getLogger(EntityManager.class.getName());

  private PooledEngine engine;
  private Entity player;
  private List<Updateable> updateables = new ArrayList();
  private List<Entity> entities = new ArrayList();
  private List<Entity> destroyEntities = new ArrayList();

  private static EntityManager INSTANCE;

  private EntityManager(PooledEngine engine, TiledMultiMapRenderer renderer, OrthographicCamera camera) {
    this.engine = engine;

    //create player
    player = new Entity();
    SpineComponent spineComponent = new SpineComponent("spines/spineboy/spineboy", "walk", 0.3f);
    DimensionComponent dimensionComponent = new DimensionComponent(spineComponent);
    player.add(spineComponent);
    player.add(dimensionComponent);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    float x = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    float y = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    PositionComponent positionComponent = new PositionComponent(x, y);
    positionComponent.x = x;
    positionComponent.y = y;
    player.add(positionComponent);

    CollisionComponent collisionComponent = new CollisionComponent(spineComponent);
    player.add(collisionComponent);
    engine.addEntity(player);

    //create systems
    SpinePositionSystem positionSystem = new SpinePositionSystem(renderer);
    engine.addSystem(positionSystem);

    SpineRenderSystem spineRenderSystem = new SpineRenderSystem(renderer);
    engine.addSystem(spineRenderSystem);

    PlayerCollisionSystem collisionSystem = new PlayerCollisionSystem(collisionComponent);
    engine.addSystem(collisionSystem);

    updateables.add(new Camera(camera, positionComponent));
    updateables.add(new MapObjectToEntityConverter(engine, renderer));
  }

  public static EntityManager create(PooledEngine engine, TiledMultiMapRenderer renderer, OrthographicCamera camera) {
    INSTANCE = new EntityManager(engine, renderer, camera);
    return INSTANCE;
  }

  public static EntityManager getInstance() {
    return INSTANCE;
  }

  public Entity getPlayer() {
    return player;
  }

  public void add(Updateable agent) { updateables.add(agent); }

  public void add(Entity entity) {
    entities.add(entity);
    engine.addEntity(entity);
  }

  public void destroy(List<Entity> toDestroy) {
    entities.removeAll(toDestroy);
    destroyEntities.addAll(toDestroy);
  }

  public void update() {
    engine.update(Gdx.graphics.getDeltaTime());

    for (Updateable updateable : updateables) {
      updateable.update();
    }

    if(!destroyEntities.isEmpty()) {
      for (Entity entity : destroyEntities) {
        engine.removeEntity(entity);
      }
      destroyEntities.clear();

      LOG.info("Ashley engine has " + engine.getEntities().size() + " entities");
    }
  }
}
