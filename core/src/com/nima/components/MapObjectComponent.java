package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.nima.render.converters.MapConstants;

/**
 * Component implementation for map objects
 */
public class MapObjectComponent implements Component, Pool.Poolable {
  public MapObject mapObject;

  public String getName() {
    return mapObject.getName();
  }

  public Vector2 getCenteredPosition() {
    return (Vector2) mapObject.getProperties().get(MapConstants.PROPERTY_CENTERED_POSITION);
  }

  @Override
  public void reset() {
    this.mapObject = null;
  }
}
