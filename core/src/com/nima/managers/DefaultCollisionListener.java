package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.nima.actors.Player;
import com.nima.actors.Spine;

/**
 * Listener to be implemented for class that need to be notified about collisions.
 */
public class DefaultCollisionListener implements CollisionListener {

  @Override
  public void collisionStart(Player player, Entity mapObjectEntity) {

  }

  @Override
  public void collisionEnd(Player player, Entity mapObjectEntity) {

  }

  @Override
  public void collisionStart(Spine spine, Entity mapObjectEntity) {

  }

  @Override
  public void collisionEnd(Spine spine, Entity mapObjectEntity) {

  }
}
