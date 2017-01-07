package com.nima.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nima.actors.Bullet;
import com.nima.components.PositionComponent;
import com.nima.components.SpriteComponent;
import com.nima.managers.EntityManager;

import static com.nima.util.Settings.BULLET_AUTO_DESTROY_DISTANCE;

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
      if(distance > BULLET_AUTO_DESTROY_DISTANCE) {
        EntityManager.getInstance().destroy(entity);
      }
    }
  }
}
