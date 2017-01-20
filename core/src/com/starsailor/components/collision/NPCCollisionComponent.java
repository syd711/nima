package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.RoutePoint;
import com.starsailor.actors.states.NPCStates;
import com.starsailor.managers.EntityManager;

/**
 * Collidable component for an ashley entity.
 */
public class NPCCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {
    if(collidee instanceof RoutePoint) {
      ((NPC) collider).getStateMachine().changeState(NPCStates.ROUTE_POINT_ARRIVED);
    }
    if(collidee instanceof Bullet) {
      Bullet bullet = (Bullet) collidee;
      if(!bullet.isOwner(collider)) {
        EntityManager.getInstance().destroy(collidee);
      }
    }
  }

  @Override
  public void reset() {

  }
}
