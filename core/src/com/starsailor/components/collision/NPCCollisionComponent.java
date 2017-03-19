package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.route.RoutePoint;
import com.starsailor.actors.states.npc.NPCStates;

/**
 * Collidable component for an ashley entity.
 */
public class NPCCollisionComponent implements Collidable, Pool.Poolable {
  private NPC npc;

  public NPCCollisionComponent(NPC npc) {
    this.npc = npc;
  }

  @Override
  public void handleCollision(Entity collidee, Vector2 position) {
    if(collidee instanceof RoutePoint) {
      npc.getStateMachine().changeState(NPCStates.ROUTE_POINT_ARRIVED);
    }
    if(collidee instanceof Bullet) {
      Bullet bullet = (Bullet) collidee;
      BulletCollisionComponent bulletCollisionComponent = bullet.getComponent(BulletCollisionComponent.class);
      bulletCollisionComponent.applyCollisionWith(npc, position);
    }
  }

  @Override
  public void reset() {
    this.npc = null;
  }
}
