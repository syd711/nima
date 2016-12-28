package com.nima.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.collect.Lists;
import com.nima.actors.*;
import com.nima.components.CollisionComponent;
import com.nima.components.LocationComponent;
import com.nima.components.MapObjectComponent;
import com.nima.render.MapChangeListener;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.systems.*;
import com.nima.util.PolygonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Central Ashley initialization of entity systems.
 */
public class EntityManager implements MapChangeListener {
  private static final Logger LOG = Logger.getLogger(EntityManager.class.getName());

  private PooledEngine engine;
  private Player player;
  private RayHandler rayHandler;
  private List<Updateable> updateables = new ArrayList<>();
  private List<Entity> destroyEntities = new ArrayList<>();

  private List<EntityClickListener> entityClickListeners = new ArrayList<>();
  private List<CollisionListener> collisionListeners = new ArrayList<>();

  private static EntityManager INSTANCE;

  private EntityManager(PooledEngine engine, TiledMultiMapRenderer renderer, World world, OrthographicCamera camera, RayHandler rayHandler) {
    this.engine = engine;
    this.rayHandler = rayHandler;

    renderer.addMapChangeListener(this);

    //create player
    player = new Player(renderer, world, rayHandler);
    addCollisionListener(player);
    engine.addEntity(player);

    //create systems
    SpinePositionSystem positionSystem = new SpinePositionSystem();
    engine.addSystem(positionSystem);

    RotationSystem rotationSystem = new RotationSystem();
    engine.addSystem(rotationSystem);

    CollisionSystem collisionSystem = new CollisionSystem(engine);
    engine.addSystem(collisionSystem);

    SpineMovementSystem movementSystem = new SpineMovementSystem();
    engine.addSystem(movementSystem);

    LightSystem lightSystem = new LightSystem();
    engine.addSystem(lightSystem);

    ScalingSystem scalingSystem = new ScalingSystem();
    engine.addSystem(scalingSystem);

    updateables.add(new AmbientLight(rayHandler));
    updateables.add(new Camera(camera, player));
    updateables.add(player);
  }

  public static EntityManager create(PooledEngine engine, TiledMultiMapRenderer renderer, World world, OrthographicCamera camera, RayHandler rayHandler) {
    INSTANCE = new EntityManager(engine, renderer, world, camera, rayHandler);
    return INSTANCE;
  }

  public static EntityManager getInstance() {
    return INSTANCE;
  }

  public Player getPlayer() {
    return player;
  }

  /**
   * Registers an entity click listener
   * @param entityClickListener
   */
  public void addEntityClickListener(EntityClickListener entityClickListener) {
    this.entityClickListeners.add(entityClickListener);
  }

  /**
   * Registers a collision listener
   * @param listener
   */
  public void addCollisionListener(CollisionListener listener) {
    this.collisionListeners.add(listener);
  }

  /**
   * Adds the given entity to the Ashley engine.
   * @param entity the entity to add
   */
  public void add(Entity entity) {
    engine.addEntity(entity);
  }

  /**
   * Registers entities to be destroyed
   * for the next render interval.
   * @param toDestroy the list of entities to be destroyed
   */
  protected void destroy(List<Entity> toDestroy) {
    destroyEntities.addAll(toDestroy);
  }

  /**
   * Uses the Ashley engine to update
   * all classes implementing the Updateable interface.
   * This can be used for non-component based refreshes.
   */
  public void update() {
    engine.update(Gdx.graphics.getDeltaTime());

    if(GameStateManager.getInstance().isPaused()) {
      return;
    }

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

  @Override
  public void mapAdded(TiledMap map, List<MapObject> mapObjects) {
    for(MapObject mapObject : mapObjects) {
      Entity entity = EntityFactory.createEntity(map, mapObject, rayHandler);
      if(entity != null) {
        engine.addEntity(entity);
      }
    }
    LOG.info("Added " + mapObjects.size() + " ashley entities.");
  }

  @Override
  public void mapRemoved(TiledMap map, List<MapObject> mapObjects) {
    ImmutableArray<Entity> entitiesFor = engine.getEntitiesFor(Family.all(MapObjectComponent.class).get());
    ArrayList<Entity> entities = Lists.newArrayList(entitiesFor);
    destroy(entities);
  }

  public void notifyCollisionStart(Entity entity, Entity mapObjectEntity) {
    for(CollisionListener collisionListener : collisionListeners) {
      if(entity instanceof Player) {
        collisionListener.collisionStart((Player)entity, mapObjectEntity);
      }
      else if(entity instanceof Spine) {
        collisionListener.collisionStart((Spine)entity, mapObjectEntity);
      }
    }
  }

  public void notifyCollisionEnd(Entity entity, Entity mapObjectEntity) {
    for(CollisionListener collisionListener : collisionListeners) {
      if(entity instanceof Player) {
        collisionListener.collisionEnd((Player)entity, mapObjectEntity);
      }
      else if(entity instanceof Spine) {
        collisionListener.collisionEnd((Spine)entity, mapObjectEntity);
      }
    }
  }

  public Entity getEntityAt(float x, float y) {
    Vector2 clickPoint = new Vector2(x, y);
    Polygon clickPolygon = PolygonUtil.clickPolygon(clickPoint);
    TiledMultiMapRenderer.debugRenderer.render("click", clickPolygon);

    //most likely a map entity
    ImmutableArray<Entity> entitiesFor = engine.getEntitiesFor(Family.all(LocationComponent.class).get());
    ArrayList<Entity> entities = Lists.newArrayList(entitiesFor);
    for(Entity entity : entities) {
      CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
      if(collisionComponent.collidesWith(clickPolygon)) {
        notifyEntityClickListeners(entity);
        return entity;
      }
    }

    return null;
  }

  // --------------- Helper ---------------------------------------------------------

  private void notifyEntityClickListeners(Entity entity) {
    for(EntityClickListener entityClickListener : this.entityClickListeners) {
      entityClickListener.entityClicked(entity);
    }
  }
}
