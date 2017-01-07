package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.actors.Bullet;
import com.nima.components.PositionComponent;
import com.nima.components.SpriteComponent;
import com.nima.managers.EntityManager;

/**
 * Used during fighting
 */
public class AutoDestroySystem extends IteratingSystem {
  public AutoDestroySystem() {
    super(Family.all(SpriteComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    if(entity instanceof Bullet) {
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      float distance = positionComponent.distanceToPlayer();
      if(distance > 2000) {
        EntityManager.getInstance().destroy(entity);
      }
    }
  }
}
