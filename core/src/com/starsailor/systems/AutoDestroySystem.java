package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Route;
import com.starsailor.components.PositionComponent;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.util.Settings;

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
      if(distance > Settings.getInstance().bullet_auto_destroy_distance) {
        EntityManager.getInstance().destroy(entity);
      }
    }
    else if(entity instanceof Route) {
      //TODO
    }
    else if(entity instanceof NPC) {
      NPC npc = (NPC) entity;
      //TODO destroy tracking point bodies!! npc.routingComponent;
    }
  }
}
