package com.starsailor.components.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.GameStateManager;
import com.starsailor.actors.*;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.actors.states.player.PlayerStates;
import com.starsailor.ui.UIManager;

/**
 * Collideable component for an ashley entity.
 */
public class PlayerCollisionComponent implements Collidable {
  @Override
  public void handleCollision(Entity collidee, Vector2 position) {
    if(collidee instanceof Location) {
      //only try to enter the location if a click was made on it, otherwise we just move over the planet
      if(Player.getInstance().getTarget() != null && Player.getInstance().getTarget().equals(collidee)) {
        //only dock to station if not in battle mode
        if(!Player.getInstance().isInBattleState()) {
          GameStateManager.getInstance().setPaused(true);
          UIManager.getInstance().getHudStage().getNavigatorPanel().setEnabled(true);
        }
      }
    }
    else if(collidee instanceof Bullet) {
      Bullet bullet = (Bullet) collidee;
      BulletCollisionComponent bulletCollisionComponent = bullet.getComponent(BulletCollisionComponent.class);
      bulletCollisionComponent.applyCollisionWith(Player.getInstance(), position);
    }
    else if(collidee instanceof Loot) {
      ((Loot) collidee).markForDestroy();
      UIManager.getInstance().getHudStage().getLootPanel().activate();
    }
    else if(collidee instanceof ClickTarget) {
      //only switch to idle when there is no target specified
      if(Player.getInstance().getTarget() == null) {
        Player.getInstance().changeState(PlayerStates.IDLE);
      }
    }
  }
}
