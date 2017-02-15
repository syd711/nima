package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Route;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.bullets.PhaserBullet;
import com.starsailor.components.GameEntityComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.util.Settings;

import java.util.List;

/**
 * Used during fighting
 */
public class AutoDestroySystem extends IteratingSystem {
  public AutoDestroySystem() {
    super(Family.all(GameEntityComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    GameEntity gameEntity = (GameEntity) entity;

    //check destroy flag
    if(gameEntity.isMarkedForDestroy()) {
      EntityManager.getInstance().destroy(entity);
    }

    if(gameEntity instanceof Bullet) {
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      float distance = positionComponent.distanceToPlayer();
      if(distance > Settings.getInstance().bullet_auto_destroy_distance) {
        EntityManager.getInstance().destroy(entity);
      }
    }
    else if(gameEntity instanceof Route) {
      //TODO
    }
    else if(gameEntity instanceof Ship) {
      Ship ship = (Ship) entity;

      if(ship.isMarkedForDestroy()) {
        //destroy phaser bullets that are still active firing on this ship
        List<PhaserBullet> entities = EntityManager.getInstance().getEntities(PhaserBullet.class);
        for(PhaserBullet phaserBullet : entities) {
          if(phaserBullet.target.equals(ship)) {
            EntityManager.getInstance().destroy(phaserBullet);
          }
        }
      }
    }
    else if(gameEntity instanceof NPC) {
      NPC npc = (NPC) entity;

    }
  }
}
