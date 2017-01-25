package com.starsailor.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.Camera;
import com.starsailor.actors.Player;
import com.starsailor.actors.Updateable;
import com.starsailor.components.BodyComponent;
import com.starsailor.components.ComponentFactory;
import com.starsailor.data.DataEntities;
import com.starsailor.render.TiledMultiMapRenderer;
import com.starsailor.systems.*;
import com.starsailor.util.Box2dUtil;

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

  private static EntityManager INSTANCE;

  private LightSystem lightSystem;

  private EntityManager() {
    this.engine = new PooledEngine();
    ComponentFactory.engine = engine;
  }

  private void init(TiledMultiMapRenderer renderer, OrthographicCamera camera, RayHandler rayHandler) {
    //create player
    player = new Player(DataEntities.getShip(DataEntities.SHIP_PLAYER));
    engine.addEntity(player);

    ScalingSystem scalingSystem = new ScalingSystem();
    engine.addSystem(scalingSystem);

    SteerableSystem steerableSystem = new SteerableSystem();
    engine.addSystem(steerableSystem);

    SpineRenderSystem renderSystem = new SpineRenderSystem(renderer.getBatch());
    engine.addSystem(renderSystem);

    AutoDestroySystem autoDestroySystem = new AutoDestroySystem();
    engine.addSystem(autoDestroySystem);

    SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(renderer.getBatch());
    engine.addSystem(spriteRenderSystem);

    RouteSystem routeSystem = new RouteSystem();
    engine.addSystem(routeSystem);

    NPCSystem npcSystem = new NPCSystem();
    engine.addSystem(npcSystem);

    BulletSystem bulletSystem = new BulletSystem();
    engine.addSystem(bulletSystem);

    ParticleSystem particleSystem = new ParticleSystem(renderer.getBatch());
    engine.addSystem(particleSystem);

    lightSystem = new LightSystem(rayHandler);
    engine.addSystem(lightSystem);

    StateMachineSystem stateMachineSystem = new StateMachineSystem();
    engine.addSystem(stateMachineSystem);

    updateables.add(new Camera(camera, player));
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
  public void addEntityListener(EntityListener listener) {
    this.engine.addEntityListener(listener);
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

    for (Updateable updateable : updateables) {
      updateable.update();
    }

    if(!destroyEntities.isEmpty()) {
      for (Entity entity : destroyEntities) {
        BodyComponent component = entity.getComponent(BodyComponent.class);
        if(component != null) {
          component.destroy();
        }

        if(entity instanceof EntityListener) {
          engine.removeEntityListener((EntityListener) entity);
        }

        engine.removeEntity(entity);
        Gdx.app.log(this.toString(), "Destroyed " + entity);
      }
      destroyEntities.clear();

      Gdx.app.log(this.toString(),"Ashley engine has " + engine.getEntities().size() + " entities");
    }
  }

  public Entity getEntityAt(float x, float y) {
    Vector2 clickPoint = new Vector2(x, y);
    return Box2dUtil.getEntityAt(Game.world, clickPoint);
  }

  public Entity getEntityAt(Vector2 pos) {
    return Box2dUtil.getEntityAt(Game.world, pos);
  }

  public ImmutableArray<Entity> getEntitiesFor(Class<? extends Component> componentClass) {
    return engine.getEntitiesFor(Family.all(componentClass).get());
  }

  public <T extends Component> T createComponent (Class<T> componentType) {
    return engine.createComponent(componentType);
  }

}
