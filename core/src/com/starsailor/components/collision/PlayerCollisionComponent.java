package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.actors.Collidable;
import com.starsailor.actors.Location;
import com.starsailor.actors.Player;
import com.starsailor.actors.Ship;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.player.FollowClickState;
import com.starsailor.actors.states.player.PlayerState;

/**
 * Collideable component for an ashley entity.
 */
public class PlayerCollisionComponent implements Collidable {
  @Override
  public void handleCollision(Entity collider, Entity collidee, Vector2 position) {
    if(collidee instanceof Location) {
      //only try to enter the location if a click was made on it, otherwise we just move over the planet
      if(Player.getInstance().target != null && Player.getInstance().target.equals(collidee)) {
        //only dock to station if not in battle mode
        if(!Player.getInstance().isInBattleState()) {
          Player.getInstance().getStateMachine().changeState(PlayerState.DOCK_TO_STATION);
        }
      }
    }
    else if(collidee instanceof Bullet) {
      Bullet bullet = (Bullet) collidee;
      BulletCollisionComponent bulletCollisionComponent = bullet.getComponent(BulletCollisionComponent.class);
      bulletCollisionComponent.applyCollisionWith(bullet, (Ship) collider, position);
    }
    else if(collidee instanceof FollowClickState.ClickTarget || collider instanceof FollowClickState.ClickTarget) {
      Player.getInstance().getStateMachine().changeState(PlayerState.IDLE);
    }
  }
}
