package com.nima.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.nima.Game;
import com.nima.actors.*;
import com.nima.components.ComponentFactory;
import com.nima.data.RouteProfile;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.systems.*;
import com.nima.util.Box2dUtil;
import com.nima.util.PolygonUtil;
import com.nima.util.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Central Ashley initialization of entity systems.
 */
public class EntityManager {
  private PooledEngine engine;
  private Player player;
  private List<Updateable> updateables = new ArrayList<>();
  private List<Entity> destroyEntities = new ArrayList<>();

  private List<EntityClickListener> entityClickListeners = new ArrayList<>();
  private List<CollisionListener> collisionListeners = new ArrayList<>();

  private static EntityManager INSTANCE;

  private LightSystem lightSystem;

  private EntityManager() {
    this.engine = new PooledEngine();
    ComponentFactory.engine = engine;
  }

  private void init(TiledMultiMapRenderer renderer, OrthographicCamera camera, RayHandler rayHandler) {
    //create player
    player = new Player();
    addCollisionListener(player);
    engine.addEntity(player);

    //create systems
    PositionSystem positionSystem = new PositionSystem();
    engine.addSystem(positionSystem);

    RotationSystem rotationSystem = new RotationSystem();
    engine.addSystem(rotationSystem);

    PlayerMovementSystem movementSystem = new PlayerMovementSystem();
    engine.addSystem(movementSystem);

    ScalingSystem scalingSystem = new ScalingSystem();
    engine.addSystem(scalingSystem);

    SteerableSystem steerableSystem = new SteerableSystem();
    engine.addSystem(steerableSystem);

    SpeedSystem speedSystem = new SpeedSystem();
    engine.addSystem(speedSystem);

    SpineRenderSystem renderSystem = new SpineRenderSystem(renderer);
    engine.addSystem(renderSystem);

    AutoDestroySystem autoDestroySystem = new AutoDestroySystem();
    engine.addSystem(autoDestroySystem);

    SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(renderer.getBatch());
    engine.addSystem(spriteRenderSystem);

    RouteSystem routeSystem = new RouteSystem();
    engine.addSystem(routeSystem);

    RoutingSystem routingSystem = new RoutingSystem();
    engine.addSystem(routingSystem);

    lightSystem = new LightSystem(rayHandler);
    engine.addSystem(lightSystem);

    updateables.add(new Camera(camera, player));
    updateables.add(player);

    //TODO
    NPC m = new NPC(player, Resources.ACTOR_SPINE, Resources.ACTOR_DEFAULT_ANIMATION, 0.2f, 300, 300);
    engine.addEntity(m);
    updateables.add(m);
  }

  public static EntityManager create(TiledMultiMapRenderer renderer, OrthographicCamera camera, RayHandler rayHandler) {
    INSTANCE = new EntityManager();
    INSTANCE.init(renderer, camera, rayHandler);
    return INSTANCE;
  }

  public static EntityManager getInstance() {
    return INSTANCE;
  }

  public Player getPlayer() {
    return player;
  }

  public LightSystem getLightSystem() {
    return lightSystem;
  }

  /**
   * Registers an entity click listener
   */
  public void addEntityClickListener(EntityClickListener entityClickListener) {
    this.entityClickListeners.add(entityClickListener);
  }

  /**
   * Registers a collision listener
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
  public void destroy(Entity toDestroy) {
    destroyEntities.add(toDestroy);
  }

  /**
   * Pauses all ashley systems
   */
  public void pauseSystems(boolean pause) {
    ImmutableArray<EntitySystem> systems = engine.getSystems();
    for(EntitySystem system : systems) {
      system.setProcessing(!pause);
    }
  }

  public void addUpdateable(Updateable entity) {
    this.updateables.add(entity);
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
        Gdx.app.log(this.toString(), "Destroyed " + entity);
      }
      destroyEntities.clear();

      Gdx.app.log(this.toString(),"Ashley engine has " + engine.getEntities().size() + " entities");
    }
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

    return Box2dUtil.getEntityAt(Game.world, clickPoint);
  }

  public boolean isRouteActive(RouteProfile routeProfile) {
    ImmutableArray<Entity> entities = engine.getEntities();
    for(Entity entity : entities) {
      if(entity instanceof Route) {
        Route route = (Route) entity;
        if(route.routeComponent.route.equals(routeProfile)) {
          return true;
        }
      }
    }
    return false;
  }
}
