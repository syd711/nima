package com.nima.managers;

import com.badlogic.ashley.core.Entity;
import com.nima.actors.Player;
import com.nima.actors.Spine;

/**
 * Listener to be implemented for class that need to be notified about collisions.
 */
public interface CollisionListener {

  /**
   * Called when the player starts/ends colliding with a MapObject entity
   * of the map.
   * @param player the player object
   * @param mapObjectEntity the MapObject the player has collided with.
   */
  void collisionStart(Player player, Entity mapObjectEntity);
  void collisionEnd(Player player, Entity mapObjectEntity);

  /**
   * Called when a spine is collision starts/ends with a MapObject entity of the map.
   * @param spine a spine on the map
   * @param mapObjectEntity the MapObject the player has collided with.
   */
  void collisionStart(Spine spine, Entity mapObjectEntity);
  void collisionEnd(Spine spine, Entity mapObjectEntity);
}
