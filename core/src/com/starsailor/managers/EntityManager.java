package com.starsailor.managers;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.*;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.route.Route;
import com.starsailor.actors.states.player.FollowClickState;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.Destroyable;
import com.starsailor.components.StatefulComponent;
import com.starsailor.render.MapManager;
import com.starsailor.systems.*;
import com.starsailor.util.box2d.Box2dUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Central Ashley initialization of galaxy systems.
 */
public class EntityManager {
  private PooledEngine engine;
  private List<GameEntity> destroyEntities = new ArrayList<>();

  private static EntityManager INSTANCE;

  private LightSystem lightSystem;

  private EntityManager() {
    this.engine = new PooledEngine();
    ComponentFactory.engine = engine;
  }

  private void init() {
    engine.addEntity(ShipFactory.createPlayer());
    EntityManager.getInstance().add(new Galaxy());

    ScalingSystem scalingSystem = new ScalingSystem();
    engine.addSystem(scalingSystem);

    SteerableSystem steerableSystem = new SteerableSystem();
    engine.addSystem(steerableSystem);

    AutoDestroySystem autoDestroySystem = new AutoDestroySystem();
    engine.addSystem(autoDestroySystem);

    RouteSystem routeSystem = new RouteSystem();
    engine.addSystem(routeSystem);

    BodyUpdateSystem bodyUpdateSystem = new BodyUpdateSystem();
    engine.addSystem(bodyUpdateSystem);

    SpineUpdateSystem spineSystem = new SpineUpdateSystem();
    engine.addSystem(spineSystem);

    BulletSystem bulletSystem = new BulletSystem();
    engine.addSystem(bulletSystem);

    ParticleSystem particleSystem = new ParticleSystem(MapManager.getInstance().getBatch());
    engine.addSystem(particleSystem);

    AnimationRenderSystem animationRenderSystem = new AnimationRenderSystem(MapManager.getInstance().getBatch());
    engine.addSystem(animationRenderSystem);

    lightSystem = new LightSystem(Game.rayHandler);
    engine.addSystem(lightSystem);

    StateMachineSystem stateMachineSystem = new StateMachineSystem();
    engine.addSystem(stateMachineSystem);

    SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem(MapManager.getInstance().getBatch());
    engine.addSystem(spriteRenderSystem);

    SpineRenderSystem renderSystem = new SpineRenderSystem(MapManager.getInstance().getBatch());
    engine.addSystem(renderSystem);

    FormationSystem formationSystem = new FormationSystem();
    engine.addSystem(formationSystem);
  }

  public static EntityManager create() {
    INSTANCE = new EntityManager();
    INSTANCE.init();
    return INSTANCE;
  }

  public static EntityManager getInstance() {
    return INSTANCE;
  }

  public LightSystem getLightSystem() {
    return lightSystem;
  }

  /**
   * Registers an galaxy click listener
   */
  public void addEntityListener(EntityListener listener) {
    this.engine.addEntityListener(listener);
  }

  /**
   * Adds the given galaxy to the Ashley engine.
   *
   * @param entity the galaxy to add
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
        ImmutableArray<Component> components = entity.getComponents();
        for(Component component : components) {
          if(component instanceof Destroyable) {
            ((Destroyable)component).destroy();
          }
        }

        if(entity instanceof EntityListener) {
          engine.removeEntityListener((EntityListener) entity);
        }

        StatefulComponent statefulComponent = entity.getComponent(StatefulComponent.class);
        if(statefulComponent != null) {
          MessageManager.getInstance().removeListener(statefulComponent.stateMachine);
        }

        engine.removeEntity(entity);

//        Gdx.app.log(this.toString(), "Destroyed " + galaxy);
      }
      destroyEntities.clear();

//      Gdx.app.log(this.toString(), "Ashley engine has " + engine.getEntities().size() + " entities");
    }
  }

  //-------------- Map Reset -----------------------------------------------------------------------

  public void resetMapEntities() {
    ImmutableArray<Entity> entities = engine.getEntities();
    for(Entity entity : entities) {
      GameEntity gameEntity = (GameEntity) entity;
      if(gameEntity instanceof Location) {
        destroy(gameEntity);
      }
      else if(gameEntity instanceof NPC) {
        destroy(gameEntity);
      }
      else if(gameEntity instanceof FormationOwner) {
        destroy(gameEntity);
      }
      else if(gameEntity instanceof Route) {
        destroy(gameEntity);
      }
      else if(gameEntity instanceof FollowClickState.ClickTarget) {
        destroy(gameEntity);
      }
      else if(gameEntity instanceof Bullet) {
        destroy(gameEntity);
      }
    }
  }

  //-------------- Entity Helper -----------------------------------------------------------------------

  public Entity getEntityAt(float x, float y) {
    Vector2 clickPoint = new Vector2(x, y);
    return Box2dUtil.getEntityAtClickPoint(Game.world, clickPoint);
  }

  public Entity getEntityAt(Vector2 pos) {
    return Box2dUtil.getEntityAtClickPoint(Game.world, pos);
  }

  public ImmutableArray<Entity> getEntitiesFor(Class<? extends Component> componentClass) {
    return engine.getEntitiesFor(Family.all(componentClass).get());
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
