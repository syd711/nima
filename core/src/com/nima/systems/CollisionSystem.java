package com.nima.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.nima.components.CollisionComponent;
import com.nima.components.MapObjectComponent;
import com.nima.components.SpineComponent;

import java.util.logging.Logger;

public class CollisionSystem extends IteratingSystem {
  private static final Logger LOG = Logger.getLogger(CollisionSystem.class.getName());

  private ComponentMapper<SpineComponent> spinesMap = ComponentMapper.getFor(SpineComponent.class);
  private Engine engine;

  public CollisionSystem(Engine engine) {
    super(Family.all(CollisionComponent.class).get());
    this.engine = engine;
  }

  public void processEntity(Entity entity, float deltaTime) {
    //check spine collisions
    SpineComponent spine = spinesMap.get(entity);
    if(spine != null) {
      CollisionComponent spineCollisionComponent = entity.getComponent(CollisionComponent.class);
      spineCollisionComponent.updateBody();

      Family mapObjectsFamily = Family.all(CollisionComponent.class).get();
      ImmutableArray<Entity> entities = engine.getEntitiesFor(mapObjectsFamily);
      for(Entity otherEntity : entities) {
        if(!otherEntity.equals(entity)) {
          CollisionComponent mapObjectCollisionComponent = otherEntity.getComponent(CollisionComponent.class);
          if(spineCollisionComponent.collidesWith(entity, mapObjectCollisionComponent)) {
            LOG.info("Collision");
          }
        }
      }
    }
  }
}