package com.nima.components.collision;

import com.badlogic.ashley.core.Entity;
import com.nima.actors.Collidable;
import com.nima.actors.Location;
import com.nima.actors.Player;
import com.nima.actors.states.PlayerState;

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
  }
}
