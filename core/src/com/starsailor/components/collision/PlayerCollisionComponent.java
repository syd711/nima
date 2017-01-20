package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.starsailor.actors.*;
import com.starsailor.actors.states.PlayerState;

/**
 * Collidable component for an ashley entity.
 */
public class PlayerCollisionComponent implements Collidable {
  @Override
  public void handleCollision(Entity collider, Entity collidee) {
    if(collidee instanceof Location) {
      if(Player.getInstance().target != null && Player.getInstance().target.equals(collidee)) {
        Player.getInstance().getStateMachine().changeState(PlayerState.DOCK_TO_STATION);
      }
    }
    else if(collidee instanceof Bullet) {
      Bullet bullet = (Bullet) collidee;
      bullet.applyCollisionWith((Player) collider);
    }
  }
}
