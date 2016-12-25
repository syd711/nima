package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;

/**
 * Component implementation for map objects
 */
public class MapObjectComponent implements Component {
  private MapObject mapObject;

  public MapObjectComponent(MapObject mapObject) {
    this.mapObject = mapObject;
  }

  public String getName() {
    return mapObject.getName();
  }

  public String getType() {
    return (String) mapObject.getProperties().get("type");
  }
}
