package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.nima.Game;
import com.nima.actors.Spine;
import com.nima.components.CollisionComponent;
import com.nima.components.LocationComponent;
import com.nima.components.SpineComponent;
import com.nima.managers.EntityManager;
import com.nima.util.PolygonUtil;

import java.util.logging.Logger;

public class CollisionSystem extends AbstractIteratingSystem {
  private static final Logger LOG = Logger.getLogger(CollisionSystem.class.getName());

  private ComponentMapper<SpineComponent> spinesMap = ComponentMapper.getFor(SpineComponent.class);
  private Engine engine;

  public CollisionSystem(Engine engine) {
    super(Family.all(CollisionComponent.class).get());
    this.engine = engine;
  }

  public void process(Entity entity, float deltaTime) {
    //check spine collisions
    SpineComponent spine = spinesMap.get(entity);
    if(spine != null) {
      checkSpineMapObjectsCollisions(entity);
    }
  }

  /**
   * Checks if the given spine entity is colliding with map objects
   *
   * @param entity the spine entity
   */
  private void checkSpineMapObjectsCollisions(Entity entity) {
    CollisionComponent spineCollision = entity.getComponent(CollisionComponent.class);

    /*
     * If the entity is a moving object like a spine, it's polygons must be refresh.
     * This is ensured by the MovementSystem.
     */
    spineCollision.collisionComponents.clear();

    PolygonUtil.createSpineBody(Game.world, (Spine) entity) ;

    Family mapObjectsFamily = Family.all(LocationComponent.class).get();
    ImmutableArray<Entity> entities = engine.getEntitiesFor(mapObjectsFamily);

    //check all map entities
    for(Entity mapEntity : entities) {
      CollisionComponent mapObject = mapEntity.getComponent(CollisionComponent.class);
      if(spineCollision.collidesWith(entity, mapObject)) {
        //check if the collision was already registered
        if(!spineCollision.isColliding(mapObject)) {
          spineCollision.addCollision(mapObject);
          EntityManager.getInstance().notifyCollisionStart(entity, mapEntity);
        }
      }
      else {
        //de-register the collision and notify listeners
        if(spineCollision.isColliding(mapObject)) {
          spineCollision.removeCollision(mapObject);
          EntityManager.getInstance().notifyCollisionEnd(entity, mapEntity);
        }
      }
    }
  }
}