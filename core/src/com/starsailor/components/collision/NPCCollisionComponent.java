package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.Bullet;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.NPC;
import com.starsailor.actors.RoutePoint;
import com.starsailor.actors.states.NPCStates;

/**
 * Collidable component for an ashley entity.
 */
public class NPCCollisionComponent implements Collidable, Pool.Poolable {
  @Override
  public void handleCollision(Entity collider, Entity collidee, Vector2 position) {
    if(collidee instanceof RoutePoint) {
      ((NPC) collider).getStateMachine().changeState(NPCStates.ROUTE_POINT_ARRIVED);
    }
    if(collidee instanceof Bullet) {
      Bullet bullet = (Bullet) collidee;
      BulletCollisionComponent bulletCollisionComponent = bullet.getComponent(BulletCollisionComponent.class);
      bulletCollisionComponent.applyCollisionWith(bullet, (NPC) collider, position);
    }
  }

  @Override
  public void reset() {

  }
}
