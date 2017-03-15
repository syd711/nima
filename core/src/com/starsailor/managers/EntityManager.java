package com.starsailor.managers;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.Game;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.actors.ShipFactory;
import com.starsailor.components.*;
import com.starsailor.render.TiledMultiMapRenderer;
import com.starsailor.systems.*;
import com.starsailor.util.box2d.Box2dUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Central Ashley initialization of entity systems.
 */
public class EntityManager implements EntityListener {
  private PooledEngine engine;
  private Player player;
  private List<GameEntity> destroyEntities = new ArrayList<>();
  private List<Body> destroyBodies = new ArrayList<>();

  private static EntityManager INSTANCE;

  private LightSystem lightSystem;

  private EntityManager() {
    this.engine = new PooledEngine();
    this.addEntityListener(this);
    ComponentFactory.engine = engine;
  }

  private void init(TiledMultiMapRenderer renderer, RayHandler rayHandler) {
    //create player
    player = ShipFactory.createPlayer();
    engine.addEntity(player);

    ScalingSystem scalingSystem = new ScalingSystem();
    engine.addSystem(scalingSystem);

    SteerableSystem steerableSystem = new SteerableSystem();
    engine.addSystem(steerableSystem);

    AutoDestroySystem autoDestroySystem = new AutoDestroySystem();
    engine.addSystem(autoDestroySystem);

    RouteSystem routeSystem = new RouteSystem();
    engine.addSystem(routeSystem);

    BattleSystem npcSystem = new BattleSystem();
    engine.addSystem(npcSystem);

    SpineUpdateSystem spineSystem = new SpineUpdateSystem();
    engine.addSystem(spineSystem);

    BulletSystem bulletSystem = new BulletSystem();
    engine.addSystem(bulletSystem);

    ParticleSystem particleSystem = new ParticleSystem(renderer.getBatch());
    engine.addSystem(particleSystem);

    AnimationRenderSystem animationRenderSystem = new AnimationRenderSystem(renderer.getBatch());
    engine.addSystem(animationRenderSystem);

    lightSystem = new LightSystem(rayHandler);
    engine.addSystem(lightSystem);

    StateMachineSystem stateMachineSystem = new StateMachineSystem();
    engine.addSystem(stateMachineSystem);

    SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(renderer.getBatch());
    engine.addSystem(spriteRenderSystem);

    SpineRenderSystem renderSystem = new SpineRenderSystem(renderer.getBatch());
    engine.addSystem(renderSystem);

    FormationSystem formationSystem = new FormationSystem();
    engine.addSystem(formationSystem);
  }

  public static EntityManager create(TiledMultiMapRenderer renderer, RayHandler rayHandler) {
    INSTANCE = new EntityManager();
    INSTANCE.init(renderer, rayHandler);
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
   *
   * @param entity the entity to add
   */
  public void add(Entity entity) {
    engine.addEntity(entity);
  }

  /**
   * Registers entities to be destroyed
   * for the next render interval.
   *
   * @param toDestroy the list of entities to be destroyed
   */
  public void destroy(GameEntity toDestroy) {
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

  /**
   * Uses the Ashley engine to update
   * all classes implementing the Updateable interface.
   * This can be used for non-component based refreshes.
   */
  public void update() {
    engine.update(Gdx.graphics.getDeltaTime());

    if(!destroyEntities.isEmpty()) {
      for(GameEntity entity : destroyEntities) {
        List<BodyComponent> components = entity.getComponents(BodyComponent.class);
        for(BodyComponent component : components) {
          component.destroy();
        }

        SteerableComponent steerableComponent = entity.getComponent(SteerableComponent.class);
        if(steerableComponent != null) {
          steerableComponent.destroy();
        }

        if(entity instanceof EntityListener) {
          engine.removeEntityListener((EntityListener) entity);
        }

        StatefulComponent statefulComponent = entity.getComponent(StatefulComponent.class);
        if(statefulComponent != null) {
          MessageManager.getInstance().removeListener(statefulComponent.stateMachine);
        }

        engine.removeEntity(entity);

//        Gdx.app.log(this.toString(), "Destroyed " + entity);
      }
      destroyEntities.clear();

//      Gdx.app.log(this.toString(), "Ashley engine has " + engine.getEntities().size() + " entities");
    }
  }

  //-------------- Entity Listener -----------------------------------------------------------------------

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity removedEntity) {
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

  public <T extends Component> T createComponent(Class<T> componentType) {
    return engine.createComponent(componentType);
  }

  public <T> List<T> getEntities(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Entity entity : engine.getEntities()) {
      if(clazz.isInstance(entity)) {
        result.add((T) entity);
      }
    }
    return result;
  }

  public void filterAliveEntities(List list) {
    Iterator iterator = list.iterator();
    while(iterator.hasNext()) {
      Object next = iterator.next();
      if(!isAliveEntity(next)) {
        iterator.remove();
      }
    }
  }

  public <T> boolean isAliveEntity(T entity) {
    if(((GameEntity)entity).isMarkedForDestroy()) {
      return false;
    }

    ImmutableArray<Entity> entities = engine.getEntities();
    for(Entity e : entities) {
      if(e.equals(entity)) {
        return true;
      }
    }
    return false;
  }

  public NPC getNpc(int itemId) {
    List<NPC> entities = getEntities(NPC.class);
    for(NPC entity : entities) {
      if(entity.getItemId() == itemId) {
        return entity;
      }
    }
    return null;
  }
}
