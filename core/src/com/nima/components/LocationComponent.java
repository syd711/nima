package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;

/**
 * Component implementation for map objects
 */
public class LocationComponent implements Component {
  private MapObject mapObject;

  public LocationComponent(MapObject mapObject) {
    this.mapObject = mapObject;
  }
}
