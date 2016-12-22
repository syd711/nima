package com.nima.render;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.List;

/**
 * Interface definition to listen map changes,
 * triggered when the player enters a new part of the map.
 */
public interface MapChangeListener {

  /**
   * Invoked for a new map that has been loaded
   * @param map the map that as been loaded
   * @param mapObjects the map objects of this map.
   */
  void mapAdded(TiledMap map, List<MapObject> mapObjects);

  /**
   * Invoked for a map that is no longer part of the combined map.
   * @param map the map that as been removed
   * @param mapObjects the map objects of this map.
   */
  void mapRemoved(TiledMap map, List<MapObject> mapObjects);
}
