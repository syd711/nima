package com.nima.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.nima.actors.Bullet;
import com.nima.actors.Collidable;
import com.nima.actors.NPC;
import com.nima.actors.RoutePoint;
import com.nima.actors.states.NPCStates;
import com.nima.managers.EntityManager;

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
