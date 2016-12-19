package com.nima.actors;

/**
 * Common interface for entities that should be updated
 * by the Ashley game engine.
 */
public interface Updateable {

  /**
   * Called for each render cycle of the game engine.
   */
  void update();
}
