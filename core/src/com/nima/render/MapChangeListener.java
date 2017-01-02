package com.nima.render;

/**
 * Interface definition to listen map changes,
 * triggered when the player enters a new part of the map.
 */
public interface MapChangeListener {

  /**
   * Invoked for a new map that has been loaded
   * @param map the map that as been loaded
   */
  void mapAdded(TiledMapFragment map);

  /**
   * Invoked for a map that is no longer part of the combined map.
   * @param map the map that as been removed
   */
  void mapRemoved(TiledMapFragment map);
}
