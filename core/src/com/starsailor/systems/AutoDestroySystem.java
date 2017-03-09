package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.starsailor.actors.*;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.bullets.PhaserBullet;
import com.starsailor.actors.route.Route;
import com.starsailor.components.GameEntityComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.SelectionManager;
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
      return;
    }

    if(gameEntity instanceof Bullet) {
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      float distance = positionComponent.distanceToPlayer();
      if(distance > Settings.getInstance().bullet_auto_destroy_distance) {
        gameEntity.markForDestroy();
      }
    }
    else if(gameEntity instanceof Route) {
      //TODO auto enable and disable of routes
    }
    else if(gameEntity instanceof FormationOwner) {
      FormationOwner formationOwner = (FormationOwner) gameEntity;
      if(formationOwner.getMembers().isEmpty()) {
        formationOwner.markForDestroy();
      }
    }

    if(gameEntity instanceof Ship) {
      Ship ship = (Ship) entity;

      if(ship.isMarkedForDestroy()) {
        //destroy phaser bullets that are still active firing on this ship
        List<PhaserBullet> entities = EntityManager.getInstance().getEntities(PhaserBullet.class);
        for(PhaserBullet phaserBullet : entities) {
          if(phaserBullet.target.equals(ship)) {
            gameEntity.markForDestroy();
          }
        }

        //remove ship from formation
        IFormationOwner formationOwner = ship.getFormationOwner();
        if(formationOwner != null && !formationOwner.equals(ship)) {
          formationOwner.removeMember(ship);
        }
      }
    }

    if(gameEntity instanceof NPC) {
      NPC npc = (NPC) entity;

      if(npc.isMarkedForDestroy()) {
        //check if we can auto-select the next member of the group
        boolean selected = npc.selectionComponent.isActive();
        if(selected) {
          SelectionManager.getInstance().setSelection(null);

          List<Ship> members = npc.getFormationMembers();
          Ship nearest = null;
          for(Ship member : members) {
            if(nearest == null) {
              nearest = member;
              continue;
            }

            if(nearest.getDistanceTo(npc) > member.getDistanceTo(npc)) {
              nearest = member;
            }
          }

          if(nearest != null) {
            SelectionManager.getInstance().setSelection((Selectable) nearest);
          }
        }
      }
    }
  }
}
