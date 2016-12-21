package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;

/**
 * Created by Matthias on 19.12.2016.
 */
public class MapObjectComponent implements Component {
  private MapObject mapObject;

  public MapObjectComponent(MapObject mapObject) {
    this.mapObject = mapObject;
  }
}
