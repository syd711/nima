package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.collect.Lists;
import com.nima.actors.Camera;
import com.nima.actors.Player;
import com.nima.actors.Spine;
import com.nima.actors.Updateable;
import com.nima.components.CollisionComponent;
import com.nima.components.MapObjectComponent;
import com.nima.render.MapChangeListener;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.systems.CollisionSystem;
import com.nima.systems.SpineMovementSystem;
import com.nima.systems.SpinePositionSystem;
import com.nima.systems.SpineRenderSystem;

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
  private World world;
  private List<Updateable> updateables = new ArrayList<>();
  private List<Entity> destroyEntities = new ArrayList<>();

  private List<CollisionListener> collisionListeners = new ArrayList<>();

  private static EntityManager INSTANCE;

  private EntityManager(PooledEngine engine, TiledMultiMapRenderer renderer, World world, OrthographicCamera camera) {
    this.engine = engine;
    this.world = world;
    renderer.addMapChangeListener(this);

    //create player
    player = new Player(world);
    engine.addEntity(player);

    //create systems
    SpinePositionSystem positionSystem = new SpinePositionSystem();
    engine.addSystem(positionSystem);

    SpineRenderSystem spineRenderSystem = new SpineRenderSystem(renderer);
    engine.addSystem(spineRenderSystem);

    CollisionSystem collisionSystem = new CollisionSystem(engine);
    engine.addSystem(collisionSystem);

    SpineMovementSystem movementSystem = new SpineMovementSystem();
    engine.addSystem(movementSystem);

    updateables.add(new Camera(camera, player));
    updateables.add(player);
  }

  public static EntityManager create(PooledEngine engine, TiledMultiMapRenderer renderer, World world, OrthographicCamera camera) {
    INSTANCE = new EntityManager(engine, renderer, world, camera);
    return INSTANCE;
  }

  public static EntityManager getInstance() {
    return INSTANCE;
  }

  public Player getPlayer() {
    return player;
  }

  public void addCollisionListener(CollisionListener listener) {
    this.collisionListeners.add(listener);
  }

  public void add(Updateable agent) { updateables.add(agent); }

  public void add(Entity entity) {
    engine.addEntity(entity);
  }

  /**
   * Registers entities to be destroyed
   * for the next render interval.
   * @param toDestroy the list of entities to be destroyed
   */
  public void destroy(List<Entity> toDestroy) {
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
      Entity entity = new Entity();
      entity.add(new MapObjectComponent(mapObject));
      entity.add(new CollisionComponent(mapObject));
      EntityManager.getInstance().add(entity);
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
}
