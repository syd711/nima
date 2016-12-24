package com.nima.managers;

import com.nima.components.CollisionComponent;

/**
 * Listener to be implemented for class that need to be notified about collisions.
 */
public interface CollisionListener {

  void collided(CollisionComponent source, CollisionComponent target);
}
