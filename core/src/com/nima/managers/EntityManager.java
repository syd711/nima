package com.nima.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.actors.*;
import com.nima.components.*;
import com.nima.render.MapConstants;
import com.nima.render.TiledMultiMapRenderer;
import com.nima.systems.*;
import com.nima.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Central Ashley initialization of entity systems.
 */
public class EntityManager {
  private static final Logger LOG = Logger.getLogger(EntityManager.class.getName());

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
  }

  private void init(TiledMultiMapRenderer renderer, OrthographicCamera camera, RayHandler rayHandler) {
    //create player
    player = new Player();
    addCollisionListener(player);
    engine.addEntity(player);

    //create systems
    SpinePositionSystem positionSystem = new SpinePositionSystem();
    engine.addSystem(positionSystem);

    RotationSystem rotationSystem = new RotationSystem();
    engine.addSystem(rotationSystem);

    SpineMovementSystem movementSystem = new SpineMovementSystem();
    engine.addSystem(movementSystem);

    ScalingSystem scalingSystem = new ScalingSystem();
    engine.addSystem(scalingSystem);

    SteerableSystem steerableSystem = new SteerableSystem();
    engine.addSystem(steerableSystem);

    SpeedSystem speedSystem = new SpeedSystem();
    engine.addSystem(speedSystem);

    SpineRenderSystem renderSystem = new SpineRenderSystem(renderer);
    engine.addSystem(renderSystem);

    ShootingSystem shootingSystem = new ShootingSystem();
    engine.addSystem(shootingSystem);

    SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(renderer.getBatch());
    engine.addSystem(spriteRenderSystem);

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
   * Pauses all ashley systems
   * @param pause
   */
  public void pauseSystems(boolean pause) {
    SpinePositionSystem positionSystem = new SpinePositionSystem();
    ImmutableArray<EntitySystem> systems = engine.getSystems();
    for(EntitySystem system : systems) {
      system.setProcessing(!pause);
    }
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

  //-------------------- Map Caching -------------------------------------------------------

//  @Override
//  public void mapAdded(TiledMap map, List<MapObject> mapObjects) {
//    for(MapObject mapObject : mapObjects) {
//      Entity entity = EntityFactory.createEntity(map, mapObject, rayHandler);
//      if(entity != null) {
//        engine.addEntity(entity);
//      }
//    }
//    LOG.info("Added " + mapObjects.size() + " ashley entities.");
//  }
//
//  @Override
//  public void mapRemoved(TiledMap map, List<MapObject> mapObjects) {
//    ImmutableArray<Entity> entitiesFor = engine.getEntitiesFor(Family.all(MapObjectComponent.class).get());
//    ArrayList<Entity> entities = Lists.newArrayList(entitiesFor);
//    destroy(entities);
//  }

  //-------------------- /Map Caching ------------------------------------------------------

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

  public <T extends Component> T createComponent (Class<T> componentType) {
    return engine.createComponent(componentType);
  }

  public MapObjectComponent addMapObjectComponent(Entity entity, MapObject mapObject) {
    MapObjectComponent component = createComponent(MapObjectComponent.class);
    component.mapObject = mapObject;
    entity.add(component);
    return component;
  }

  public BodyComponent addBodyComponent(Entity entity, MapObject mapObject) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = (Body) mapObject.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
    entity.add(component);
    return component;
  }

  public BodyComponent addBodyComponent(BodyEntity entity, PositionComponent positionComponent, Sprite sprite) {
    BodyComponent component = createComponent(BodyComponent.class);
    component.body = Box2dUtil.createSpriteBody(positionComponent, Game.world, sprite);
    component.body.setUserData(entity);
    entity.add(component);
    return component;
  }

  public BodyComponent addBodyComponent(Spine spine) {
    BodyComponent component = createComponent(BodyComponent.class);
    Body body = Box2dUtil.createSpineBody(Game.world, spine);
    component.body = body;
    component.body.setUserData(spine);
    spine.add(component);
    return component;
  }

  public ScalingComponent addScalingComponent(Spine spine) {
    ScalingComponent component = createComponent(ScalingComponent.class);
    component.init(1f);
    spine.add(component);
    return component;
  }

  public SpeedComponent addSpeedComponent(Spine spine) {
    SpeedComponent component = createComponent(SpeedComponent.class);
    component.init(Settings.MAX_ACTOR_SPEED);
    spine.add(component);
    return component;
  }

  public LightComponent addLightComponent(Entity entity) {
    LightComponent component = createComponent(LightComponent.class);
    entity.add(component);
    return component;
  }

  public SpineComponent addSpineComponent(Spine spine) {
    SpineComponent component = createComponent(SpineComponent.class);
    spine.add(component);
    return component;
  }

  public RotationComponent addRotationComponent(Spine spine) {
    RotationComponent component = createComponent(RotationComponent.class);
    component.spine = spine;
    spine.add(component);
    return component;
  }

  public MovementComponent addMovementComponent(Spine spine) {
    MovementComponent component = createComponent(MovementComponent.class);
    component.setSpine(spine);
    spine.add(component);
    return component;
  }

  public PositionComponent addPositionComponent(Entity entity, boolean initCentered, float heightOffset) {
    PositionComponent component = createComponent(PositionComponent.class);
    if(initCentered) {
      Vector2 screenCenter = GraphicsUtil.getScreenCenter(heightOffset);
      component.x = screenCenter.x;
      component.y = screenCenter.y;
    }
    entity.add(component);
    return component;
  }

  public SpriteComponent addSpriteComponent(Entity entity, String resourceLocation) {
    SpriteComponent component = createComponent(SpriteComponent.class);
    component.setTextures(new Texture(resourceLocation));
    entity.add(component);
    return component;
  }

  public BulletDamageComponent addBulletDamageComponent(Entity entity, int damage) {
    BulletDamageComponent component = createComponent(BulletDamageComponent.class);
    component.damage = damage;
    entity.add(component);
    return component;
  }
}
